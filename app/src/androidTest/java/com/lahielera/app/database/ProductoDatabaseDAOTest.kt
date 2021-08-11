package com.lahielera.app.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.lahielera.app.model.Producto
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.google.common.truth.Truth.*
import com.lahielera.app.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ProductoDatabaseDAOTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ProductoDatabase
    private lateinit var dao: ProductoDatabaseDAO

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                ProductoDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.productoDatabaseDAO
    }

    @After
    fun teardown(){
        database.close()
    }

    @Test
    fun insertProduct() = runBlocking{
        val product = Producto(
                nombre = "producto de prueba",
                cantidad = 2,
                categoria = "test",
                marca = "no se",
                precio = 100.00,
                id = 1
        )
        dao.insert(product)
        val allProducts = dao.getAllProductos().getOrAwaitValue()
        assertThat(allProducts).contains(product)
    }

}