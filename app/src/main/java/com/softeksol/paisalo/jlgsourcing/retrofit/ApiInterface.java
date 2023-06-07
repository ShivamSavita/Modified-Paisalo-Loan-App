package com.softeksol.paisalo.jlgsourcing.retrofit;


import com.google.gson.JsonObject;
import com.softeksol.paisalo.dealers.Models.BrandResponse;
import com.softeksol.paisalo.dealers.Models.CreatorAllResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    /*@FormUrlEncoded
    @POST("token")
    public Call<LoginResponse> login(@Field("grant_type") String grantType, @Field("username") String  userName, @Field("password") String password);

*/
    @POST("CrifReport/CheckCrifReport")
    public  Call<CheckCrifData> checkCrifScore(@Body JsonObject object);


    @POST("CrifReport/GetCrifReport")
    public  Call<ScrifData> getCrifScore(@Body JsonObject object);



    @POST("IdentityVerification/Get")
    public Call<JsonObject> cardValidate(@Body JsonObject object);

    @GET("PFL.ABF.API/api/Masters/GetBrands")
    Call<BrandResponse> getBrands(@Header("Authorization") String authHeader);

   @GET("PFL.ABF.API/api/Oem/Getpincode")
    Call<BrandResponse> getPINData(@Query("pincode") int textnum, @Header("Authorization") String authHeader);

   @POST("PFL.ABF.API/api/Oem/CreateOem")
    Call<ResponseBody> saveOEmDetails( @Header("Authorization") String authHeader,@Body JsonObject jsonObject);

    @GET("PFL.ABF.API/api/Oem/GetOems")
    Call<BrandResponse> getOEMList(@Header("Authorization") String authHeader);

    @GET("PFL.ABF.API/api/Oem/getProductByOemId")
    Call<BrandResponse> getProductList(@Header("Authorization") String authHeader,@Query("id") int id);

    @GET("PFL.ABF.API/api/LiveTrack/SourcingStatus")
    Call<ResponseBody> postDataForEsignTrack(@Header("Authorization") String authHeader,@Query("ficode") String ficode,@Query("creator") String creator);

    @POST("PDL.Mobile.API/api/LiveTrack/CreateDocDataFetch")
    Call<ResponseBody> saveFetchedDocData(
            @Header("Authorization") String authHeader,@Body JsonObject jsonObject

    );
    @GET("PDL.Mobile.API/api/LiveTrack/GetAppLink")
    Call<BrandResponse> getAppLinkStatus(@Query("version") String version,@Query("AppName") String AppName,@Query("action") int action);


    @GET("PDL.UserService.API/api/DDLHelper/GetCreator")
    Call<List<CreatorAllResponse>> getAllCreator();

    @POST("PFL.ABF.API/api/Oem/CreateOemnew")
    Call<BrandResponse> createCreator(@Header("Authorization") String authHeader,@Body JsonObject jsonObject);

    @GET("PFL.ABF.API/api/Masters/GetVehicleType")
    Call<BrandResponse> getVehicleType(@Header("Authorization") String authHeader);

    @GET("PFL.ABF.API/api/Oem/GetBankDetaisByOemId")
    Call<BrandResponse> getBankList(@Header("Authorization") String authHeader,@Query("id") int id);


    @POST("PFL.ABF.API/api/Oem/CreateBankDetails")
    Call<BrandResponse> uploadBankOemDetails(@Header("Authorization") String authHeader,@Body RequestBody file);

    @GET("PFL.ABF.API/api/Dealer/GetOems")
    Call<BrandResponse> getOEMbyCreator(@Header("Authorization") String authHeader,@Query("Creator") String creator);
   @POST("PFL.ABF.API/api/Dealer/CreateDealer")
    Call<BrandResponse> createDealer(@Header("Authorization") String authHeader,@Body JsonObject jsonObject);

   @GET("PFL.ABF.API/api/Dealer/GetDealers")
    Call<BrandResponse> getAllDealers(@Header("Authorization") String authHeader);

   @GET("PFL.ABF.API/api/Dealer/GetABFDocs")
    Call<BrandResponse> getABfDocs(@Header("Authorization") String authHeader,@Query("Type") String Type,@Query("id") int id);


  @POST("PFL.ABF.API/api/Oem/PreDocumentUpload")
    Call<BrandResponse> uploadDealerPreDocs(@Header("Authorization") String authHeader,@Body RequestBody file);














}
