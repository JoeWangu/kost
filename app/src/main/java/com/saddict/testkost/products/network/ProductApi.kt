package com.saddict.testkost.products.network

import com.saddict.testkost.products.model.remote.PostProduct
import com.saddict.testkost.products.model.remote.Product
import com.saddict.testkost.products.model.remote.ProductResults
import com.saddict.testkost.products.model.remote.RegisterUser
import com.saddict.testkost.products.model.remote.RegisterUserResponse
import com.saddict.testkost.products.model.remote.User
import com.saddict.testkost.products.model.remote.UserResponse
import com.saddict.testkost.utils.Constants.CREATE_USER_URL
import com.saddict.testkost.utils.Constants.LOGIN_URL
import com.saddict.testkost.utils.Constants.PRODUCTS_URL
import com.saddict.testkost.utils.Constants.SINGLE_PRODUCTS_URL
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

///inventory/api/products/?page=2

interface ProductApi {
    @GET(PRODUCTS_URL)
    suspend fun getProducts(
        @Query("format")format: String,
        @Query("page") page: Int,
    ): Product

    @GET(SINGLE_PRODUCTS_URL)
    suspend fun getSingleProduct(@Path("id") id: Int) : ProductResults

    @POST(PRODUCTS_URL)
    suspend fun postProducts(@Body body: PostProduct): Response<ProductResults>

    @PUT(SINGLE_PRODUCTS_URL)
    suspend fun updateProduct(
        @Path("id") id: Int,
        @Body body: PostProduct
    ): Response<ProductResults>

    @POST(CREATE_USER_URL)
    suspend fun register(@Body user: RegisterUser): Response<RegisterUserResponse>

    @POST(LOGIN_URL)
    suspend fun login(@Body user: User) : Response<UserResponse>
}