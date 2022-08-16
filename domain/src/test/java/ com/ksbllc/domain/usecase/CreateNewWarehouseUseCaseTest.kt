package  com.ksbllc.domain.usecase

import com.ksbllc.domain.models.Warehouse
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

class CreateNewWarehouseUseCaseTest {

    val rep = mock<FirebaseRepository>()

    @Test
    fun `should return the same data as in repository`(){
        val scope =
            createTestCoroutineScope(TestCoroutineDispatcher() + TestCoroutineExceptionHandler() + EmptyCoroutineContext)
        val job = scope.launch {
            Mockito.`when`(rep.createNewWarehouse(Warehouse("name", 10000f, null))).thenReturn(true)

            val useCase = CreateNewWarehouseUseCase(rep)
            val actual = useCase.execute(Warehouse("name", 10000f, null))
            val expected = true
            Assertions.assertEquals(expected, actual)
        }
    }

}