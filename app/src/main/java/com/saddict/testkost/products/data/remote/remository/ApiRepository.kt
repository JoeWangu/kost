package com.saddict.testkost.products.data.remote.remository

import com.saddict.testkost.products.model.remote.PostProduct
import com.saddict.testkost.products.model.remote.Product
import com.saddict.testkost.products.model.remote.ProductResults
import com.saddict.testkost.products.model.remote.RegisterUser
import com.saddict.testkost.products.model.remote.RegisterUserResponse
import com.saddict.testkost.products.model.remote.User
import com.saddict.testkost.products.model.remote.UserResponse
import retrofit2.Response

interface ApiRepository {
    suspend fun getProducts(page: Int): Product

    suspend fun getSingleProduct(id: Int) : ProductResults

    suspend fun postProducts(body: PostProduct): Response<ProductResults>

    suspend fun updateProduct(id: Int, body: PostProduct) : Response<ProductResults>

    suspend fun register(user: RegisterUser): Response<RegisterUserResponse>

    suspend fun login(user: User) : Response<UserResponse>
}