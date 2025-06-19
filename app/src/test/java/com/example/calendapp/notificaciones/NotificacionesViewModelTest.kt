package com.example.calendapp.notificaciones

import com.example.calendapp.notificaciones.viewmodel.NotificacionesViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class NotificacionesViewModelTest {

    private lateinit var viewModel: NotificacionesViewModel
    @Mock
    private lateinit var mockDb: FirebaseFirestore
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        viewModel = NotificacionesViewModel(mockDb)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be empty list`() = runTest {
        // When
        val notificaciones = viewModel.notificaciones.first()

        // Then
        assertTrue(notificaciones.isEmpty())
    }

   
} 