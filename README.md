# CloudyWallpaper! 😁
An wallpaper open source application. I built this app for study purpose, this app now only support for **Android 12 (API 31)**

I implemented **Dynamic Color Material Design**
![light_dyanmic](https://user-images.githubusercontent.com/68740152/175291642-7d4674a1-44b4-4f3d-ad90-4b8aab73de5a.jpeg)

![dynamic](https://user-images.githubusercontent.com/68740152/175291780-66c6689a-face-486f-951f-88bca930b808.jpeg)

Splash screen


![light_splash](https://user-images.githubusercontent.com/68740152/175305217-3f992876-6728-43ab-b047-ad0cb8326d8b.jpeg)

![dark_splash](https://user-images.githubusercontent.com/68740152/175305247-43a5e2cd-0c47-4694-9a73-5915de9b13a5.jpeg)

You can search whatever wallpapers you want, then you can save to your gallery and share your favorite wallpaper
![search](https://user-images.githubusercontent.com/68740152/175292848-7238e521-bbcc-4a98-9d3f-fbb9c4c73a42.jpeg)

![download](https://user-images.githubusercontent.com/68740152/175293725-310a4027-6fbd-4534-8b3a-f82c52adb839.jpeg)

![share](https://user-images.githubusercontent.com/68740152/175293804-dcc3b2af-4ba1-4a9f-8fca-b1ab8516d73c.jpeg)

I also implemented unit test for API response
``` kotlin
@Test
fun wallpaper_api_service() {
    // arrange
    mockResponse("wallpapers.json")
    runBlocking {
        // action
        val apiResponse = wallpaperApiService.getWallpapersByKeyword(query, perPage)

        // assert
        Assert.assertNotNull(apiResponse)
        Assert.assertTrue("API response wasn't empty", apiResponse.wallpapers.isNotEmpty())
        Assert.assertEquals(
            "photographer name didn't match",
            "Luis del Río",
             apiResponse.wallpapers.first().photographer
        )
     }
  }
``` 

Thanks to ***Pexels API[^1]***

[^1]: https://www.pexels.com/api/
