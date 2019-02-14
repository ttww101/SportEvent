package com.yaming.android.sportsevent;

import android.content.Context;
import android.util.ArrayMap;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;

public class ShowApiKey {

    private static ShowApiKey instance;
    private static int currentIndex;
    private static ArrayList<String> appIdList,appKeyList;
    public static ShowApiKey getInstance() {
        if (instance == null) {
            instance = new ShowApiKey();
        }
        return instance;
    }

    private ShowApiKey() {
        currentIndex = 0;
        appIdList = new ArrayList<>();
        appKeyList = new ArrayList<>();

        appIdList.add("86922");
        appKeyList.add("1ba6860ace9e47528f074ed74766ce7d");

        appIdList.add("86942");
        appKeyList.add("2f58eea804bf43e8a35ddb4bd806e15d");

        appIdList.add("86947");
        appKeyList.add("4cd214bcda4e401390d1dd184ef9d563");
    }

    public String getAppId() {
        return appIdList.get(currentIndex);
    }
    public String getAppKey() {
        return appKeyList.get(currentIndex);
    }
    public boolean changeAppKeyStore() {
        int beforeIndex = currentIndex;
        if ((currentIndex+1)<appIdList.size()){
            currentIndex = currentIndex+1;
        } else {
            currentIndex = 0;
        }
        if (currentIndex == beforeIndex) {
            return false;
        } else {
            return true;
        }
    }

    public void httpGetHupuNews(final Context context, final ArrayMap<String,String> headerProperties, final HttpThreadCallback httpCallBack) {
        String urlGet = "http://route.showapi.com/967-1?showapi_appid="+ ShowApiKey.getInstance().getAppId()+"&showapi_sign="+ShowApiKey.getInstance().getAppKey();
        HttpThreadHandler.get(context, urlGet, headerProperties, new HttpThreadCallback() {
            @Override
            public void runCallback(int runStatus, ArrayMap<String, String> responseHeaderProperties, String result, String error) {
                int resCode = 99;
                try {
                    JSONObject jo = new JSONObject(result);
                    resCode = jo.getInt("showapi_res_code");
                } catch (JSONException e) {

                }
                if (resCode == 0) {
                    httpCallBack.runCallback(runStatus,responseHeaderProperties,result,error);
                } else {
                    ShowApiKey.getInstance().changeAppKeyStore();
                    httpGetHupuNews(context,headerProperties,httpCallBack);
                }
            }
        });
    }

    public void httpGetNews(final Context context, final int eventId, final int pageNum, final ArrayMap<String,String> headerProperties, final HttpThreadCallback httpCallBack) {
        String urlGet = "http://route.showapi.com/1677-7?showapi_appid="+ ShowApiKey.getInstance().getAppId()+"&showapi_sign="+ShowApiKey.getInstance().getAppKey()+"&eventId=" + eventId + "&page="+pageNum;
        HttpThreadHandler.get(context, urlGet, headerProperties, new HttpThreadCallback() {
            @Override
            public void runCallback(int runStatus, ArrayMap<String, String> responseHeaderProperties, String result, String error) {
                int resCode = 99;
                try {
                    JSONObject jo = new JSONObject(result);
                    resCode = jo.getInt("showapi_res_code");
                } catch (JSONException e) {

                }
                if (resCode == 0) {
                    httpCallBack.runCallback(runStatus,responseHeaderProperties,result,error);
                } else {
                    ShowApiKey.getInstance().changeAppKeyStore();
                    httpGetNews(context,eventId,pageNum,headerProperties,httpCallBack);
                }
            }
        });
    }

    public void httpGetRecents(final Context context, final int eventId, final int seasonNum, final int pageNum, final ArrayMap<String,String> headerProperties, final HttpThreadCallback httpCallBack) {
        String urlGet = "http://route.showapi.com/1677-4?showapi_appid="+ ShowApiKey.getInstance().getAppId()+"&showapi_sign="+ShowApiKey.getInstance().getAppKey()+"&eventId=" + eventId + "&season="+seasonNum+"&page=" + pageNum;
        HttpThreadHandler.get(context, urlGet, headerProperties, new HttpThreadCallback() {
            @Override
            public void runCallback(int runStatus, ArrayMap<String, String> responseHeaderProperties, String result, String error) {
                int resCode = 99;
                try {
                    JSONObject jo = new JSONObject(result);
                    resCode = jo.getInt("showapi_res_code");
                } catch (JSONException e) {

                }
                if (resCode == 0) {
                    httpCallBack.runCallback(runStatus,responseHeaderProperties,result,error);
                } else {
                    ShowApiKey.getInstance().changeAppKeyStore();
                    httpGetRecents(context,eventId,seasonNum,pageNum,headerProperties,httpCallBack);
                }
            }
        });
    }

    public void getRecentSeasonPageListLoop(final Context context, final int eventId, final int season, final ArrayMap<String,String> headerProperties, final HttpThreadCallback httpCallBack) {
        String urlGet = "http://route.showapi.com/1677-4?showapi_appid="+ ShowApiKey.getInstance().getAppId()+"&showapi_sign="+ShowApiKey.getInstance().getAppKey()+"&eventId=" + eventId + "&season="+season+"&page=1";
        HttpThreadHandler.get(context, urlGet, headerProperties, new HttpThreadCallback() {
            @Override
            public void runCallback(int runStatus, ArrayMap<String, String> responseHeaderProperties, String result, String error) {
                int resCode = 99;
                try {
                    JSONObject jo = new JSONObject(result);
                    resCode = jo.getInt("showapi_res_code");
                } catch (JSONException e) {

                }
                if (resCode == 0) {
                    httpCallBack.runCallback(runStatus,responseHeaderProperties,result,error);
                } else {
                    ShowApiKey.getInstance().changeAppKeyStore();
                    getRecentSeasonPageListLoop(context,eventId,season,headerProperties,httpCallBack);
                }
            }
        });
    }

    public void getRecentSeasonPageList(final Context context,final int eventId, final SeasonPageListCallback seasonPageListCallback) {

        final ArrayList<SeasonPage> sasonPageList = new ArrayList<>();
        final int season00 = Calendar.getInstance().get(Calendar.YEAR);
        ShowApiKey.getInstance().getRecentSeasonPageListLoop(context, eventId, season00, new ArrayMap<String, String>(), new HttpThreadCallback() {
            @Override
            public void runCallback(int runStatus00, ArrayMap<String, String> responseHeaderProperties00, String result00, String error00) {

                int allPages00 = 0;
                try {

                    JSONObject jo00 = new JSONObject(result00);
                    JSONObject recentsJo00 = jo00.getJSONObject("showapi_res_body");
                    allPages00 = recentsJo00.getInt("allPages");

                } catch (JSONException e) {}

                for(int i=allPages00;i>0;i--) {
                    SeasonPage seasonPage00 = new SeasonPage(season00,i);
                    sasonPageList.add(seasonPage00);
                }

                final int season01 = Calendar.getInstance().get(Calendar.YEAR)-1;
                ShowApiKey.getInstance().getRecentSeasonPageListLoop(context, eventId, season01, new ArrayMap<String, String>(), new HttpThreadCallback() {
                    @Override
                    public void runCallback(int runStatus01, ArrayMap<String, String> responseHeaderProperties01, String result01, String error01) {

                        int allPages01 = 0;
                        try {

                            JSONObject jo01 = new JSONObject(result01);
                            JSONObject recentsJo01 = jo01.getJSONObject("showapi_res_body");
                            allPages01 = recentsJo01.getInt("allPages");

                        } catch (JSONException e) {}

                        for(int j=allPages01;j>0;j--) {
                            SeasonPage seasonPage01 = new SeasonPage(season01,j);
                            sasonPageList.add(seasonPage01);
                        }

                        final int season02 = Calendar.getInstance().get(Calendar.YEAR)-2;
                        String urlGet02 = "http://route.showapi.com/1677-4?showapi_appid="+ ShowApiKey.getInstance().getAppId()+"&showapi_sign="+ShowApiKey.getInstance().getAppKey()+"&eventId=" + eventId + "&season="+season02+"&page=1";
                        ShowApiKey.getInstance().getRecentSeasonPageListLoop(context, eventId, season02, new ArrayMap<String, String>(), new HttpThreadCallback() {
                            @Override
                            public void runCallback(int runStatus02, ArrayMap<String, String> responseHeaderProperties02, String result02, String error02) {

                                int allPages02 = 0;
                                try {

                                    JSONObject jo02 = new JSONObject(result02);
                                    JSONObject recentsJo02 = jo02.getJSONObject("showapi_res_body");
                                    allPages02 = recentsJo02.getInt("allPages");

                                } catch (JSONException e) {}

                                for(int k=allPages02;k>0;k--) {
                                    SeasonPage seasonPage02 = new SeasonPage(season02,k);
                                    sasonPageList.add(seasonPage02);
                                }

                                seasonPageListCallback.runCallback(sasonPageList);

                            }
                        });

                    }
                });

            }
        });

    }

    public void httpGetHistorical(final Context context, final int eventId, final int seasonNum, final int pageNum, final ArrayMap<String,String> headerProperties, final HttpThreadCallback httpCallBack) {
        String urlGet = "http://route.showapi.com/1677-3?showapi_appid="+ ShowApiKey.getInstance().getAppId()+"&showapi_sign="+ShowApiKey.getInstance().getAppKey()+"&eventId=" + eventId + "&season="+seasonNum+"&page=" + pageNum;
        HttpThreadHandler.get(context, urlGet, headerProperties, new HttpThreadCallback() {
            @Override
            public void runCallback(int runStatus, ArrayMap<String, String> responseHeaderProperties, String result, String error) {
                int resCode = 99;
                try {
                    JSONObject jo = new JSONObject(result);
                    resCode = jo.getInt("showapi_res_code");
                } catch (JSONException e) {

                }
                if (resCode == 0) {
                    httpCallBack.runCallback(runStatus,responseHeaderProperties,result,error);
                } else {
                    ShowApiKey.getInstance().changeAppKeyStore();
                    httpGetHistorical(context,eventId,seasonNum,pageNum,headerProperties,httpCallBack);
                }
            }
        });
    }

    public void getHistoricalSeasonPageListLoop(final Context context, final int eventId, final int season, final ArrayMap<String,String> headerProperties, final HttpThreadCallback httpCallBack) {
        String urlGet = "http://route.showapi.com/1677-3?showapi_appid="+ ShowApiKey.getInstance().getAppId()+"&showapi_sign="+ShowApiKey.getInstance().getAppKey()+"&eventId=" + eventId + "&season="+season+"&page=1";
        HttpThreadHandler.get(context, urlGet, headerProperties, new HttpThreadCallback() {
            @Override
            public void runCallback(int runStatus, ArrayMap<String, String> responseHeaderProperties, String result, String error) {
                int resCode = 99;
                try {
                    JSONObject jo = new JSONObject(result);
                    resCode = jo.getInt("showapi_res_code");
                } catch (JSONException e) {

                }
                if (resCode == 0) {
                    httpCallBack.runCallback(runStatus,responseHeaderProperties,result,error);
                } else {
                    ShowApiKey.getInstance().changeAppKeyStore();
                    getHistoricalSeasonPageListLoop(context,eventId,season,headerProperties,httpCallBack);
                }
            }
        });
    }

    public void getHistoricalSeasonPageList(final Context context,final int eventId, final SeasonPageListCallback seasonPageListCallback) {

        final ArrayList<SeasonPage> sasonPageList = new ArrayList<>();
        final int season00 = Calendar.getInstance().get(Calendar.YEAR)-1;
        ShowApiKey.getInstance().getHistoricalSeasonPageListLoop(context, eventId, season00, new ArrayMap<String, String>(), new HttpThreadCallback() {
            @Override
            public void runCallback(int runStatus00, ArrayMap<String, String> responseHeaderProperties00, String result00, String error00) {

                int allPages00 = 0;
                try {

                    JSONObject jo00 = new JSONObject(result00);
                    JSONObject historicalJo00 = jo00.getJSONObject("showapi_res_body");
                    allPages00 = historicalJo00.getInt("allPages");

                } catch (JSONException e) {}

                for(int i=allPages00;i>0;i--) {
                    SeasonPage seasonPage00 = new SeasonPage(season00,i);
                    sasonPageList.add(seasonPage00);
                }

                final int season01 = Calendar.getInstance().get(Calendar.YEAR)-2;
                ShowApiKey.getInstance().getHistoricalSeasonPageListLoop(context, eventId, season01, new ArrayMap<String, String>(), new HttpThreadCallback() {
                    @Override
                    public void runCallback(int runStatus01, ArrayMap<String, String> responseHeaderProperties01, String result01, String error01) {

                        int allPages01 = 0;
                        try {

                            JSONObject jo01 = new JSONObject(result01);
                            JSONObject historicalJo01 = jo01.getJSONObject("showapi_res_body");
                            allPages01 = historicalJo01.getInt("allPages");

                        } catch (JSONException e) {}

                        for(int j=allPages01;j>0;j--) {
                            SeasonPage seasonPage01 = new SeasonPage(season01,j);
                            sasonPageList.add(seasonPage01);
                        }

                        final int season02 = Calendar.getInstance().get(Calendar.YEAR)-3;
                        ShowApiKey.getInstance().getHistoricalSeasonPageListLoop(context, eventId, season02, new ArrayMap<String, String>(), new HttpThreadCallback() {
                            @Override
                            public void runCallback(int runStatus02, ArrayMap<String, String> responseHeaderProperties02, String result02, String error02) {

                                int allPages02 = 0;
                                try {

                                    JSONObject jo02 = new JSONObject(result02);
                                    JSONObject historicalJo02 = jo02.getJSONObject("showapi_res_body");
                                    allPages02 = historicalJo02.getInt("allPages");

                                } catch (JSONException e) {}

                                for(int k=allPages02;k>0;k--) {
                                    SeasonPage seasonPage02 = new SeasonPage(season02,k);
                                    sasonPageList.add(seasonPage02);
                                }

                                seasonPageListCallback.runCallback(sasonPageList);

                            }
                        });

                    }
                });

            }
        });

    }

    public void httpGetShooter(final Context context, final int eventId, final ArrayMap<String,String> headerProperties, final HttpThreadCallback httpCallBack) {
        String urlGet = "http://route.showapi.com/1677-6?showapi_appid="+ ShowApiKey.getInstance().getAppId()+"&showapi_sign="+ShowApiKey.getInstance().getAppKey()+"&eventId=" + eventId;
        HttpThreadHandler.get(context, urlGet, headerProperties, new HttpThreadCallback() {
            @Override
            public void runCallback(int runStatus, ArrayMap<String, String> responseHeaderProperties, String result, String error) {
                int resCode = 99;
                try {
                    JSONObject jo = new JSONObject(result);
                    resCode = jo.getInt("showapi_res_code");
                } catch (JSONException e) {

                }
                if (resCode == 0) {
                    httpCallBack.runCallback(runStatus,responseHeaderProperties,result,error);
                } else {
                    ShowApiKey.getInstance().changeAppKeyStore();
                    httpGetShooter(context,eventId,headerProperties,httpCallBack);
                }
            }
        });
    }

    public void httpGetRanking(final Context context, final int eventId, final ArrayMap<String,String> headerProperties, final HttpThreadCallback httpCallBack) {
        String urlGet = "http://route.showapi.com/1677-5?showapi_appid="+ ShowApiKey.getInstance().getAppId()+"&showapi_sign="+ShowApiKey.getInstance().getAppKey()+"&eventId=" + eventId;
        HttpThreadHandler.get(context, urlGet, headerProperties, new HttpThreadCallback() {
            @Override
            public void runCallback(int runStatus, ArrayMap<String, String> responseHeaderProperties, String result, String error) {
                int resCode = 99;
                try {
                    JSONObject jo = new JSONObject(result);
                    resCode = jo.getInt("showapi_res_code");
                } catch (JSONException e) {

                }
                if (resCode == 0) {
                    httpCallBack.runCallback(runStatus,responseHeaderProperties,result,error);
                } else {
                    ShowApiKey.getInstance().changeAppKeyStore();
                    httpGetRanking(context,eventId,headerProperties,httpCallBack);
                }
            }
        });
    }

}
