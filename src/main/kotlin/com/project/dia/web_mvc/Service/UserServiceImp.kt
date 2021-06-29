package com.project.dia.web_mvc.Service

import com.project.dia.utils.JwtUtils
import com.project.dia.utils.UserTokenId
import com.project.dia.web_mvc.Component.OnCreateAccountEvent
import com.project.dia.web_mvc.Component.OnPasswordResetEvent
import com.project.dia.web_mvc.Component.UserUtils
import com.project.dia.web_mvc.Model.Entity.AccountType
import com.project.dia.web_mvc.Model.Entity.PasswordResetEntity
import com.project.dia.web_mvc.Model.Entity.RolesEntity
import com.project.dia.web_mvc.Model.Entity.UserEntity
import com.project.dia.web_mvc.Model.Model_Request.FacebookModel
import com.project.dia.web_mvc.Model.Model_Request.GoogleModel
import com.project.dia.web_mvc.Model.Model_Request.RegisterModel
import com.project.dia.web_mvc.Model.Model_Request.UpdateDataModel
import com.project.dia.web_mvc.Model.Model_Response.ModelResponse
import com.project.dia.web_mvc.Model.Model_Response.UserSuccess
import com.project.dia.web_mvc.config_security.UserPrincipal
import com.project.dia.web_mvc.constants.SecurityKey
import com.project.dia.web_mvc.repository.PasswordResetRepository
import com.project.dia.web_mvc.repository.RoleRepository
import com.project.dia.web_mvc.repository.UserDao.UserDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*
import javax.servlet.http.HttpServletResponse
import kotlin.collections.HashSet
import kotlin.collections.set

@Service
class UserServiceImp : UserService {
    var securityKey: SecurityKey = SecurityKey()
    private var jwtUtils: JwtUtils = JwtUtils()

    @Autowired
    lateinit var userDao: UserDao

    @Autowired
    private lateinit var eventPublisher: ApplicationEventPublisher

    @Autowired
    lateinit var utils: UserUtils

    @Autowired
    lateinit var bCryptPasswordEncoder: BCryptPasswordEncoder

    @Autowired
    lateinit var passwordResetRepository: PasswordResetRepository

    @Autowired
    lateinit var userId: UserTokenId

    override fun findAllUser(): MutableList<UserEntity> {
        return userDao.findAll()
    }

    @Autowired
    lateinit var roleRepository: RoleRepository

    override fun findUserByEmailAndSendResponse(email: String, response: HttpServletResponse): ModelResponse {
        val user = userDao.findUserByEmail(email)
        val claims = HashMap<String, Any>()
        claims["account_type"] = user.login.toString()
        val token: String? = jwtUtils.generateToken(user.email, claims)
        jwtUtils.addCookieToResponse(token, response)
        return ModelResponse(
            success = true, message = "Successful authenticated!",
            dataUserResponse = UserSuccess(
                firstName = user.firstName,
                email = user.email,
                account_type = user.login,
                role = "GUEST"
            )
        )
    }


    override fun saveUser(user: RegisterModel): ModelResponse {
        val userExists = userDao.checkUserEmailExist(user.email)
        return if (userExists < 1) {
            user.password = bCryptPasswordEncoder.encode(user.password)
            val roleEntities: MutableCollection<RolesEntity> = HashSet()
            for (role: String in HashSet(arrayListOf("ROLE_GUEST"))) {
                val rolesEntity: RolesEntity? = roleRepository.findByName(role)
                if (rolesEntity != null) {
                    roleEntities.add(rolesEntity)
                }
            }

            val userIdToken: String = userId.generateUserId(9)
            user.userId = userIdToken
            val emailToken: String = userId.generateEmailToken(userIdToken)
            val userEntity = UserEntity(
                email = user.email,
                firstName = user.firstName,
                lastName = user.lastName,
                password = user.password,
                roles = roleEntities,
                UserId = userIdToken, emailStatus = false, emailToken = emailToken, login = "basic"
            )
            val account = AccountType(type = securityKey.BASIC, user = userEntity)
            userEntity.add(account = account)

            userDao.saveUser(userEntity);
            eventPublisher.publishEvent(OnCreateAccountEvent(appUrl = "/dia-aplication", account = userEntity))

            ModelResponse(
                success = true,
                message = "You are registered!",
                UserSuccess(
                    firstName = user.firstName,
                    email = user.email,
                    phone = user.phone,
                    account_type = userEntity.login,
                    role = "GUEST"
                )
            )
        } else {
            ModelResponse(success = false, message = "You are not authenticated!", null)
        }
    }

    override fun updateUser(user: UpdateDataModel): ModelResponse {
        val userExists = userDao.checkUserEmailExist(user.email)
        return if (userExists > 0) {
            val userEntity: UserEntity = userDao.findUserByEmail(user.email)
            userEntity.firstName = user.firstName
            userEntity.lastName = user.lastName
            userEntity.email = user.email
            userEntity.phone = user.phone
            userDao.updateUser(userEntity)
            ModelResponse(
                success = true,
                message = "You are authenticated!",
                UserSuccess(
                    firstName = user.firstName,
                    lastName = user.lastName,
                    email = user.email,
                    phone = user.phone,
                    account_type = userEntity.login
                )
            )

        } else {
            ModelResponse(success = false, message = "You are not authenticated!", null)
        }

    }

    override fun findUserByEmail(email: String): UserEntity {
        return userDao.findUserByEmail(email)

    }

    override fun validateEmailToken(token: String): ModelResponse {
        println(token)
        val user: UserEntity? = userDao.findUserByEmailToken(token)
        if (user != null) {
            println(user.emailToken)
        }
        if (user != null) {
            val hasExpired: Boolean = userId.checkExpirationToken(token)
            println(hasExpired)
            if (!hasExpired) {
                user.emailStatus = true
                user.emailToken = null.toString();
                userDao.updateUser(user)
                return ModelResponse(
                    success = true,
                    message = "You are authenticated!",
                    UserSuccess(firstName = user.firstName, email = user.email, account_type = user.login)
                )
            }
        }
        return ModelResponse(success = false, message = "You are not authenticated!", null)
    }


    override fun requestResetPassword(email: String): ModelResponse {
        val userEntity: UserEntity = userDao.findUserByEmail(email)
        if (userEntity != null) {
            val token: String? = userId.generatePasswordToken(userEntity.UserId)
            val passwordResetTokenEntity = PasswordResetEntity(token = token, userDetails = userEntity)
            passwordResetRepository.save(passwordResetTokenEntity)
            eventPublisher.publishEvent(
                OnPasswordResetEvent(
                    account = passwordResetTokenEntity,
                    appUrl = "/dia-aplication"
                )
            )
            return ModelResponse(
                success = true,
                message = "The request was send on your email!",
                UserSuccess(firstName = userEntity.firstName, email = userEntity.email, account_type = userEntity.login)
            )
        }
        return ModelResponse(success = false, message = "Invalid email", null)
    }

    override fun accountData(email: String): ModelResponse {
        return if (userDao.checkUserEmailExist(email) > 0) {
            val userEntity: UserEntity = userDao.findUserByEmail(email)
            ModelResponse(
                success = true,
                message = "Account data",
                dataUserResponse = UserSuccess(
                    firstName = userEntity.firstName,
                    email = userEntity.email,
                    lastName = userEntity.lastName,
                    phone = userEntity.phone,
                    account_type = userEntity.login
                )
            )
        } else {
            ModelResponse(success = false, message = "Invalid email", null)
        }
    }

    override fun resetPassword(token: String, password: String): ModelResponse {
        val hasEcpired: Boolean = userId.checkExpirationToken(token)
        val passwordResetEntity: PasswordResetEntity = passwordResetRepository.findByToken(token)

        if (!hasEcpired) {
            val encryptPassword = bCryptPasswordEncoder.encode(password)
            val userEntity: UserEntity = passwordResetEntity.userDetails
            userEntity.password = encryptPassword
            val saveUser = userDao.saveUser(userEntity)
            if (saveUser != null && saveUser.password.equals(encryptPassword)) {
                passwordResetRepository.delete(passwordResetEntity)
                return ModelResponse(
                    success = true,
                    message = "Your Password successfuly changed!",
                    UserSuccess(
                        firstName = userEntity.firstName,
                        email = userEntity.email,
                        account_type = userEntity.login
                    )
                )
            }
        }
        passwordResetRepository.delete(passwordResetEntity)
        return ModelResponse(success = false, message = "Password can't changed!", null)
    }

    override fun registerWithGoogle(googleModel: GoogleModel, response: HttpServletResponse): ModelResponse {
        if (userDao.checkUserEmailExist(googleModel.email) < 1) {
            val roleEntities: MutableCollection<RolesEntity> = HashSet()
            for (role: String in HashSet(arrayListOf("ROLE_GUEST"))) {
                val rolesEntity: RolesEntity? = roleRepository.findByName(role)
                if (rolesEntity != null) {
                    roleEntities.add(rolesEntity)
                }
            }
            val userIdToken: String = userId.generateUserId(9)
            val userEntity = UserEntity(
                email = googleModel.email,
                firstName = googleModel.firstName,
                lastName = googleModel.lastName,
                password = null,
                roles = roleEntities,
                UserId = userIdToken, emailStatus = true, login = "google"
            )
            val account = AccountType(type = securityKey.GOOGLE, user = userEntity)
            userEntity.add(account = account)
            userDao.saveUser(userEntity);
        } else {
            val userEntity: UserEntity = userDao.findUserByEmail(googleModel.email)
            userEntity.login = "google"
            userDao.updateUser(userEntity)
        }
        val claims = HashMap<String, Any>()
        claims["account_type"] = securityKey.GOOGLE
        val token: String? = jwtUtils.generateToken(googleModel.email, claims)
        jwtUtils.addCookieToResponse(token, response)
        return ModelResponse(
            success = true, message = "You are authenticated!",
            UserSuccess(
                firstName = googleModel.firstName,
                email = googleModel.email,
                account_type = securityKey.GOOGLE,
                role = "GUEST"
            )
        )

    }

    override fun updateUserData(userEntity: UserEntity) {
        userDao.updateUser(userEntity)
    }

    override fun registerWithFacebook(facebookModel: FacebookModel, response: HttpServletResponse): ModelResponse {
        if (userDao.checkUserEmailExist(facebookModel.email) < 1) {
            val roleEntities: MutableCollection<RolesEntity> = HashSet()
            for (role: String in HashSet(arrayListOf("ROLE_GUEST"))) {
                val rolesEntity: RolesEntity? = roleRepository.findByName(role)
                if (rolesEntity != null) {
                    roleEntities.add(rolesEntity)
                }
            }
            val userIdToken: String = userId.generateUserId(9)
            val userEntity = UserEntity(
                email = facebookModel.email,
                firstName = facebookModel.firstName,
                lastName = facebookModel.lastName,
                password = null,
                roles = roleEntities,
                UserId = userIdToken, emailStatus = true, login = "facebook"
            )
            val account = AccountType(type = securityKey.FACEBOOK, user = userEntity)
            userEntity.add(account = account)
            userDao.saveUser(userEntity);
        } else {
            val userEntity: UserEntity = userDao.findUserByEmail(facebookModel.email)
            userEntity.login = "facebook"
            userDao.updateUser(userEntity)
        }
        val claims = HashMap<String, Any>()
        claims["account_type"] = securityKey.FACEBOOK
        val token: String? = jwtUtils.generateToken(facebookModel.email, claims)
        jwtUtils.addCookieToResponse(token, response)
        return ModelResponse(
            success = true, message = "You are authenticated!",
            UserSuccess(
                firstName = facebookModel.firstName,
                email = facebookModel.email, account_type = securityKey.FACEBOOK, role = "GUEST"
            )
        )
    }

    override fun loadUserByUsername(email: String): UserDetails {
        val userEntity: UserEntity = userDao.findUserByEmail(email) ?: throw UsernameNotFoundException(email)
            ?: throw UsernameNotFoundException(email)
        return UserPrincipal(userEntity = userEntity)
    }

}