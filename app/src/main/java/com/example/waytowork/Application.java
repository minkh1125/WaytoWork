package com.example.waytowork;

public class Application extends android.app.Application {

    private Application app;
    public ClassSharedPreference preference;

    @Override
    public void onCreate() {
        super.onCreate();


        app = this; // this가 이 클라스를 지칭
        preference = new ClassSharedPreference(getApplicationContext());
        //        Context를 해당 Class로 캐스팅 해줘야 NullPointerException이 발생하지 않습니다.
        //        Activity 클래스는 Context 클래스를 상속 확장하였으므로, Context를 얻기 위해 Activity의 this를 직접 받아올 수도 있습니다.
        //        하지만 무분별한 Activity 참조는 메모리릭이 발생할 확률을 높이므로 가급적 getApplicationContext() 메서드를 통해 얻는것이 좋습니다.
        //        ClassSharedPreference 클래스에서 가져다 씀
    }

}
