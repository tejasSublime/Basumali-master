package com.tech.Education.Cognitive.Webservices;

import com.tech.Education.Cognitive.Model.Account.AccountResponse;
import com.tech.Education.Cognitive.Model.AffiliateModel;
import com.tech.Education.Cognitive.Model.ArticlesModel;
import com.tech.Education.Cognitive.Model.BrochureModel;
import com.tech.Education.Cognitive.Model.Category.CategoryResponse;
import com.tech.Education.Cognitive.Model.CategoryVideos.VideoByCategoryResponse;
import com.tech.Education.Cognitive.Model.CoursesCategoryModel;
import com.tech.Education.Cognitive.Model.DayDetails;
import com.tech.Education.Cognitive.Model.DayDetailsViewModel;
import com.tech.Education.Cognitive.Model.EventsModel;
import com.tech.Education.Cognitive.Model.GeneralVideoModel;
import com.tech.Education.Cognitive.Model.HomeBannerModel;
import com.tech.Education.Cognitive.Model.MVideo;
import com.tech.Education.Cognitive.Model.NotificationModel;
import com.tech.Education.Cognitive.Model.OfferModel;
import com.tech.Education.Cognitive.Model.PromoVideoModel;
import com.tech.Education.Cognitive.Model.PurchaseResponse;
import com.tech.Education.Cognitive.Model.PurchasedVideoResponse;
import com.tech.Education.Cognitive.Model.QuizModel;
import com.tech.Education.Cognitive.Model.RedeemModel;
import com.tech.Education.Cognitive.Model.ResponseModel;
import com.tech.Education.Cognitive.Model.VideoByCategory;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by SAMI on 4/7/2018.
 */

public interface Services {

    @GET("/winners/api.php?action=Video&categoryid=2&perpage=7")
    Call<ResponseModel> login();

    /*@GET("/winners/api.php")
    Call<ResponseModel> registerUser( @Query("action") String action,
                                      @Query("user_fullname") String user_fullname,
                                      @Query("user_email") String user_email,
                                      @Query("user_password") String user_password,
                                      @Query("user_mobile") String user_mobile,
                                      @Query("user_mobile_code") String user_mobile_code,
                                      @Query("refer_code") String refer_code);
*/
    @GET("/winners/api.php?action=Video&categoryid=2&perpage=7")
    Call<GeneralVideoModel> getSalesMarketingCourses();

    @GET("/winners/api.php?action=VideoByCategory")
    Call<VideoByCategoryResponse> getVideoByCategory(
            @Query("categoryid") String categoryid,
            @Query("perpage") String perpage
    );


    @GET("/winners/api.php?action=Video&categoryid=2&perpage=100")
    Call<GeneralVideoModel> getAllSalesMarketingCourses();

    @GET("/winners/api.php?action=Video&categoryid=3&perpage=7")
    Call<GeneralVideoModel> getLeaderShipAndMasteryVideo();

    @GET("/winners/api.php?action=Video&categoryid=3&perpage=100")
    Call<GeneralVideoModel> getAllLeaderShipAndMasteryVideo();


    @GET("/winners/api.php?action=Video&categoryid=4&perpage=7")
    Call<GeneralVideoModel> getBusinneEducationVideo();

    @GET("/winners/api.php?action=Video&categoryid=4&perpage=100")
    Call<GeneralVideoModel> getAllBusinneEducationVideo();


    @GET("/winners/api.php?action=Offer")
    Call<OfferModel> getOffersFromServer();

    @GET("/winners/api.php?action=Articles")
    Call<ArticlesModel> getArticlesFromServer();

    @GET("/winners/api.php?action=Event")
    Call<EventsModel> getEventsFromServer();

    @GET("/winners/api.php?action=Brochures")
    Call<BrochureModel> getBrochures();
    // http://winnerspaathshala.com/winners/api.php?action=Brochures

    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);

    @GET("/winners/api.php?action=QuizData")
    Call<QuizModel> getAllQuizFromServer();

    @GET
    Call<ResponseBody> downLoadBitmap(@Url String fileUrl);

    @POST
    Call<ResponseBody> sendAppOpenPoints(@Url String fileUrl);

    @GET("/winners/api.php?action=Redeem")
    Call<RedeemModel> getAllRedeemsOffers();

    @GET("/winners/api.php?action=GetUserWallet")
    Call<AccountResponse> getPoints(
            @Query("userid") String userId
    );

    @POST("/product/api/getAttendanceOnDate")
    Call<List<DayDetails>> getDayDetailsOfSalesPerson(@Body DayDetailsViewModel dayDetailsViewModel);

    @GET("/winners/api.php?action=purchase_individual_item")
    Call<PurchaseResponse> postIndividualPurchaseDetails(@Query("userid") String userId,
                                                         @Query("orderId") String orderId,
                                                         @Query("packageName") String packageName,
                                                         @Query("productId") String productId,
                                                         @Query("videoid") String videoid,
                                                         @Query("purchaseTime") String purchaseTime,
                                                         @Query("purchaseState") String purchaseState,
                                                         @Query("purchaseToken") String purchaseToken);


    @GET("/winners/api.php?action=purchase_category")
    Call<PurchaseResponse> postCategoryPurchaseDetails(@Query("userid") String userId,
                                                       @Query("orderId") String orderId,
                                                       @Query("packageName") String packageName,
                                                       @Query("productId") String productId,
                                                       @Query("purchaseTime") String purchaseTime,
                                                       @Query("purchaseState") String purchaseState,
                                                       @Query("purchaseToken") String purchaseToken);


//winners/api.php?action=getpurchasecategory&userid=2
    //http://winnerspaathshala.com/winners/api.php?action=getindividual_itemById&userid=1&videoid=1

    @GET("/winners/api.php")
    Call<MVideo> getCategoryPurchasedVideo(@Query("action") String action,
                                           @Query("userid") String userId
    );


    @GET("/winners/api.php")
    Call<MVideo> getIndividualPurchasedVideo(@Query("action") String action,
                                             @Query("userid") String userId
    );


    @GET("/winners/api.php?action=Affiliate")
    Call<AffiliateModel> getAffiliatesData();


    @GET("/winners/api.php?action=Banner")
    Call<HomeBannerModel> getBannersFromServer();


    @GET("/winners/api.php?action=Promo")
    Call<PromoVideoModel> getPromosVideos();


    @GET("/winners/api.php?action=Category")
    Call<CategoryResponse> getCategory();

    @GET("/winners/api.php?action=getpurchasecategory")
    Call<CoursesCategoryModel> getCategoryCourse(
            @Query("userid") String userid,
            @Query("productId") String productId
    );


    // http://winnerspaathshala.com/winners/api.php?action=getpurchasecategory&userid=2&productId=2


    @GET("/winners/api.php?action=Video")
    Call<VideoByCategory> getAllVideos(@Query("categoryid") String categoryid,
                                       @Query("perpage") String perpage);


    @GET("/winners/api.php")
    Call<PurchasedVideoResponse> getMyCoursesCategoryWise(@Query("action") String action,
                                                          @Query("userid") String userID,
                                                          @Query("productId") String catId);


    @GET("/winners/api.php?action=generatekeyvideo")
    Call<ResponseModel> purchaseVideoFromCode(@Query("purchase_program_key") String purchase_program_key,
                                              @Query("videoid") String videoid,
                                              @Query("userid") String userid);


    @GET("/winners/api.php?action=generatekeycategory")
    Call<ResponseModel> purchaseCategoryFromCode(@Query("purchase_program_key") String purchase_program_key,
                                                 @Query("productId") String productId,
                                                 @Query("userid") String userid);

    @GET("/winners/api.php?action=check_redeem_points")
    Call<ResponseModel> redeemFromPoints(@Query("user_id") String user_id,
                                         @Query("type") String type,
                                         @Query("id") String id);


    @GET("/winners/api.php?action=Fetchevent")
    Call<EventsModel> getEventsDetails(@Query("start_date") String start_date);


    @GET("winners/api.php?action=purchase_event")
    Call<ResponseModel> purchaseEvent(@Query("userid") String userid,
                                      @Query("orderId") String orderId,
                                      @Query("productId") String eventId,
                                      @Query("purchaseTime") String purchaseTime,
                                      @Query("purchaseToken") String purchaseToken);

    @GET("/winners/api.php?action=get_notification")
    Call<NotificationModel> getNotificationListFromServer();


    @GET("/winners/api.php?action=check_purchase_googleplay")
    Call<ResponseModel> checkPurchaseState(@Query("user_id") String userid,
                                           @Query("type") String type,
                                           @Query("id") String productId);

    @GET("/winners/api.php?action=getindividual_itemById")
    Call<PurchasedVideoResponse> getindividual_itemById(@Query("userid") String userid);

    @GET("/winners/api.php")
    Call<ResponseModel> postOfferPoint(@Query("action") String action,
                                       @Query("userid") String userid,
                                       @Query("productId") String productId);
}
