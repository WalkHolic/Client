만든 사람 : 이해찬
설명 : 프론트와 백 통신할때 사용되는 클래스들 입니다.

ServerRequestApi : 서비스 인터페이스 정의 (나중에 ParkService, RoadServcie 등으로 분리 가능성 있음)
ServiceGenerator : Activity에서 Retrofit2 사용하실때 이걸로 빌드하시면 자동으로 리퀘스트 헤더에 토큰을 넣어줍니다.
Autentication뭐시기 : ServiceGenerator에서 사용하는 클래스인데 모르셔도 상관없을 것 같습니다. 걍 토큰 넣어주는 클래스.


Activity에서 사용 예시입니다.

protected void onCreate(Bundle savedInstanceState) {
...
...
String TOKEN = getToken(); // access token을 가져오는 함수를 직접 정의하셔야합니다.
storeService = ServiceGenerator.createService(StoreService.class, TOKEN);
...
...
...
loadStores();
}

prviate void loadStores() {
storeService.getStoreListOrderByGrade().enqueue(new Callback<List<StoreDto>>() {
@Override
public void onResponse(Call<List<StoreDto>> call,
Response<List<StoreDto>> response) {
if (response.isSuccessful()) {
// response.body()
// response.body()에서 넘어오는 데이터로 Adapter에 뿌려주기
} else {
Log.d("REST FAILED MESSAGE", response.message());
}
}

            @Override
            public void onFailure(Call<List<StoreDto>> call, Throwable t) {
                Log.d("REST ERROR!", t.getMessage());
            }

}

참고: https://velog.io/@prayme/Retrofit2%EB%A1%9C-JWT-%EC%9D%B8%EC%A6%9D%ED%95%98%EA%B8%B0