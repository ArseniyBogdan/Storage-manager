//package com.example.ksbllc.presentation.viewModels
//
//import com.google.android.gms.common.internal.Asserts
//import com.ksbllc.domain.repository.FirebaseRepository
//import com.ksbllc.domain.usecase.AuthentificationUseCase
//import com.ksbllc.domain.usecase.RegistrationUseCase
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.test.TestCoroutineDispatcher
//import kotlinx.coroutines.test.TestCoroutineExceptionHandler
//import kotlinx.coroutines.test.createTestCoroutineScope
//import org.junit.jupiter.api.AfterEach
//import org.junit.jupiter.api.Assertions
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.mockito.Mockito
//import org.mockito.kotlin.mock
//import kotlin.coroutines.EmptyCoroutineContext
//
//class RegistrationActivityVMTest {
//
//    private val rep = mock<FirebaseRepository>()
//    private val registrationUseCase = mock<RegistrationUseCase>()
//    private val registrationActivityVM = RegistrationActivityVM()
//
//
//
//    @BeforeEach
//    fun beforeEach(){
//
//    }
//
//    @AfterEach
//    fun afterEach(){
//        Mockito.reset(registrationUseCase)
//    }
//
//    @Test
//    fun `should return true if all is good`(){
//
//    }
//
//    @Test
//    fun `should return false if all is bad`(){
//
//    }
//
//    @Test
//    fun `should return false if the phoneNumber is entered incorrectly`(){
//        val name = "Arseniy"
//        val surname = "Bogdan"
//        val phoneNumber = ""
//
//        val actual = registrationActivityVM.get_isEmptyFields()
//        expected =
//        Assertions.assertEquals()
//    }
//
//    @Test
//    fun `should return false if the password is entered incorrectly`(){
//
//    }
//
//    @Test
//    fun `should return false if the email is entered incorrectly`(){
//
//    }
//
//    @Test
//    fun `should return false if the all data is entered correctly`(){
//
//    }
//
//
//}