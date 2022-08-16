package  com.ksbllc.domain.usecase

import org.junit.jupiter.params.ParameterizedTest
import com.ksbllc.domain.repository.FirebaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineExceptionHandler
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.createTestCoroutineScope
import kotlin.coroutines.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.kotlin.mock

class AuthentificationUseCaseTest {

    val rep = mock<FirebaseRepository>()

    @Test
    fun `should return the same data as in repository`(){
        val scope =
            createTestCoroutineScope(TestCoroutineDispatcher() + TestCoroutineExceptionHandler() + EmptyCoroutineContext)
        val job = scope.launch {
            Mockito.`when`(rep.signIn(email = "email", password = "password")).thenReturn(true)
            val useCase = AuthentificationUseCase(rep)
            val actual = useCase.execute(email = "email", password = "password")
            val expected = true
            Assertions.assertEquals(expected, actual)
        }
    }

}