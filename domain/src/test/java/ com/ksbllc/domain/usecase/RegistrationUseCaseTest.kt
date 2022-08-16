package  com.ksbllc.domain.usecase

import com.ksbllc.domain.repository.FirebaseRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineExceptionHandler
import kotlinx.coroutines.test.createTestCoroutineScope
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import kotlin.coroutines.EmptyCoroutineContext

class RegistrationUseCaseTest {

    val rep = mock<FirebaseRepository>()

    @Test
    fun `should return the same data as in repository`(){
        val scope =
            createTestCoroutineScope(TestCoroutineDispatcher() + TestCoroutineExceptionHandler() + EmptyCoroutineContext)
        val job = scope.launch {
            Mockito.`when`(rep.registration(email = "email", password = "password",
                firstName = "kek", secondName = "kek")).thenReturn(true)

            val useCase = RegistrationUseCase(rep)
            val actual = useCase.execute(email = "email", password = "password",
                firstName = "kek", secondName = "kek")
            val expected = true
            Assertions.assertEquals(expected, actual)
        }
    }

}