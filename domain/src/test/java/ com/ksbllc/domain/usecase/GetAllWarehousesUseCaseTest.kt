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

class GetAllWarehousesUseCaseTest {

    val rep = mock<FirebaseRepository>()

    @Test
    fun `should return the same data as in repository`(){
        val scope =
            createTestCoroutineScope(TestCoroutineDispatcher() + TestCoroutineExceptionHandler() + EmptyCoroutineContext)
        val job = scope.launch {
            Mockito.`when`(rep.getAllWarehouses(accessValue = "all")).
                thenReturn(arrayListOf(Warehouse("name", 10000f, null)))

            val useCase = GetAllWarehousesUseCase(rep)
            val actual = useCase.execute(accessLVL = "all")
            val expected = Warehouse("name", 10000f, null)
            Assertions.assertEquals(expected, actual)
        }
    }

}