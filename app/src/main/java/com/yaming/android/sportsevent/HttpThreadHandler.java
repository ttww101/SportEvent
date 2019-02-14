package com.yaming.android.sportsevent;

import android.content.Context;
import android.os.Handler;
import android.util.ArrayMap;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by SW_Lin on 2015/6/10.
 */
public class HttpThreadHandler {

    private static int URL_MALFORMED_ERROR = 903;
    private static int PROTOCAL_ERROR = 904;
    private static int IO_ERROR = 905;

    private static int HTTP_SUCCESS = 200;
    private static int URL_NOT_FOUND = 404;
    private static int SERVER_ERROR = 490;
    private static int NETWORK_PROCESS_ERROR = 500;

    private static Handler mHandler = new Handler();

    public static void threadRun(Context context, ThreadRunCallback threadRunCallback) {
        threadRunCallback.execute(context);
    };
    public abstract class ThreadRunCallback {
        public void execute(final Context context) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (context != null) {
                        runCallback();
                    } else {
                        Log.d("ErrorLog","Thread stop because context null");
                    }
                }
            };
            new Thread(runnable).start();
        }
        public abstract void runCallback();
    }

    public static void handlerPost(Context context, Handler handler, HandlerPostCallback handlerPostCallback) {
        handlerPostCallback.execute(context, handler);
    };

    public abstract class HandlerPostCallback {
        public void execute(final Context context, Handler handler) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (context != null) {
                        postCallback();
                    } else {
                        Log.d("ErrorLog","Handler  stop because context null");
                    }
                }
            });
        }
        public abstract void postCallback();
    }

    public static void get(final Context context, final String urlGet, final ArrayMap<String,String> headerProperties, final HttpThreadCallback httpCallBack) {
        threadRun(context, new HttpThreadHandler().new ThreadRunCallback() {
            @Override
            public void runCallback() {

                HttpURLConnection conn = null;
                try {
                    // 建立連線
                    URL url = new URL(urlGet);
                    trustAllHosts();
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-Type", "application/json");
                    if (headerProperties != null) {
                        for (int i=0;i<headerProperties.size();i++) {
                            conn.setRequestProperty(headerProperties.keyAt(i), headerProperties.valueAt(i));
                        }
                    }
                    conn.setRequestProperty("Content-Length", "0");
                    conn.setDoInput(true); // 開啟接收資料
                    int runStatus = conn.getResponseCode();

                    final ArrayMap<String,String> responseHeaderProperties = new ArrayMap<>();
                    for (int i=0;i<conn.getHeaderFields().size();i++) {
                        responseHeaderProperties.put(conn.getHeaderFieldKey(i),conn.getHeaderField(i));
                    }
                    if (runStatus == 200) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        reader.close();
                        final String result = sb.toString();

                        handlerPost(context, mHandler, new HttpThreadHandler().new HandlerPostCallback() {
                            @Override
                            public void postCallback() {
                                httpCallBack.runCallback(HttpThreadHandler.HTTP_SUCCESS, responseHeaderProperties, result, "");
                            }
                        });
                    } else if (runStatus == 404) {
                        handlerPost(context, mHandler, new HttpThreadHandler().new HandlerPostCallback() {
                            @Override
                            public void postCallback() {
                                httpCallBack.runCallback(HttpThreadHandler.URL_NOT_FOUND, responseHeaderProperties, "", "UrlNotFoundException");
                            }
                        });
                    } else if (runStatus == 490) {
                        if (conn.getHeaderField("error_code") != null) {
                            final String errorCode = conn.getHeaderField("error_code");
                            handlerPost(context, mHandler, new HttpThreadHandler().new HandlerPostCallback() {
                                @Override
                                public void postCallback() {
                                    httpCallBack.runCallback(HttpThreadHandler.SERVER_ERROR, responseHeaderProperties, "", errorCode);
                                }
                            });
                        } else {
                            handlerPost(context, mHandler, new HttpThreadHandler().new HandlerPostCallback() {
                                @Override
                                public void postCallback() {
                                    httpCallBack.runCallback(HttpThreadHandler.SERVER_ERROR, responseHeaderProperties, "", "ServerProcessException");
                                }
                            });
                        }
                    } else if (runStatus >= 400 && runStatus < 600) {
                        handlerPost(context, mHandler, new HttpThreadHandler().new HandlerPostCallback() {
                            @Override
                            public void postCallback() {
                                httpCallBack.runCallback(HttpThreadHandler.NETWORK_PROCESS_ERROR, responseHeaderProperties, "", "NetworkProcessException");
                            }
                        });
                    }
                } catch (MalformedURLException mue) {
                    mue.printStackTrace();
                    handlerPost(context, mHandler, new HttpThreadHandler().new HandlerPostCallback() {
                        @Override
                        public void postCallback() {
                            httpCallBack.runCallback(HttpThreadHandler.URL_MALFORMED_ERROR, new ArrayMap<String,String>(), "", "MalformedURLException");
                        }
                    });
                } catch (ProtocolException pe) {
                    pe.printStackTrace();
                    handlerPost(context, mHandler, new HttpThreadHandler().new HandlerPostCallback() {
                        @Override
                        public void postCallback() {
                            httpCallBack.runCallback(HttpThreadHandler.PROTOCAL_ERROR, new ArrayMap<String,String>(), "", "ProtocolException");
                        }
                    });
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    handlerPost(context, mHandler, new HttpThreadHandler().new HandlerPostCallback() {
                        @Override
                        public void postCallback() {
                            httpCallBack.runCallback(HttpThreadHandler.IO_ERROR, new ArrayMap<String,String>(), "", "IOException");
                        }
                    });
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }

            }
        });
    }

    public static void post(final Context context, final String urlPost, final ArrayMap<String,String> headerProperties, final String dataPost, final HttpThreadCallback httpCallBack) {
        threadRun(context, new HttpThreadHandler().new ThreadRunCallback() {
            @Override
            public void runCallback() {
                HttpURLConnection conn = null;
                try {
                    // 建立連線
                    URL url = new URL(urlPost);
                    trustAllHosts();
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    if (headerProperties != null) {
                        for (int i=0;i<headerProperties.size();i++) {
                            conn.setRequestProperty(headerProperties.keyAt(i), headerProperties.valueAt(i));
                        }
                    }
                    conn.setRequestProperty("Content-Length", String.valueOf(dataPost.getBytes().length));
                    conn.setDoOutput(true); // 開啟上傳資料
                    conn.setDoInput(true); // 開啟接收資料
                    OutputStream os = conn.getOutputStream();
                    os.write(dataPost.getBytes("UTF-8"));
                    os.flush();
                    os.close();
                    int runStatus = conn.getResponseCode();
                    final ArrayMap<String,String> responseHeaderProperties = new ArrayMap<>();
                    for (int i=0;i<conn.getHeaderFields().size();i++) {
                        responseHeaderProperties.put(conn.getHeaderFieldKey(i),conn.getHeaderField(i));
                    }
                    if (runStatus == 200) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        reader.close();
                        final String result = sb.toString();
                        handlerPost(context, mHandler, new HttpThreadHandler().new HandlerPostCallback() {
                            @Override
                            public void postCallback() {
                                httpCallBack.runCallback(HttpThreadHandler.HTTP_SUCCESS, responseHeaderProperties, result, "");
                            }
                        });
                    } else if (runStatus == 404) {
                        handlerPost(context, mHandler, new HttpThreadHandler().new HandlerPostCallback() {
                            @Override
                            public void postCallback() {
                                httpCallBack.runCallback(HttpThreadHandler.URL_NOT_FOUND, responseHeaderProperties, "", "UrlNotFoundException");
                            }
                        });
                    } else if (runStatus == 490) {
                        if (conn.getHeaderField("error_code") != null) {
                            final String errorCode = conn.getHeaderField("error_code");
                            handlerPost(context, mHandler, new HttpThreadHandler().new HandlerPostCallback() {
                                @Override
                                public void postCallback() {
                                    httpCallBack.runCallback(HttpThreadHandler.SERVER_ERROR, responseHeaderProperties, "", errorCode);
                                }
                            });
                        } else {
                            handlerPost(context, mHandler, new HttpThreadHandler().new HandlerPostCallback() {
                                @Override
                                public void postCallback() {
                                    httpCallBack.runCallback(HttpThreadHandler.SERVER_ERROR, responseHeaderProperties, "", "ServerProcessException");
                                }
                            });
                        }
                    } else if (runStatus >= 400 && runStatus < 600) {
                        handlerPost(context, mHandler, new HttpThreadHandler().new HandlerPostCallback() {
                            @Override
                            public void postCallback() {
                                httpCallBack.runCallback(HttpThreadHandler.NETWORK_PROCESS_ERROR, responseHeaderProperties, "", "NetworkProcessException");
                            }
                        });
                    }
                } catch (MalformedURLException mue) {
                    mue.printStackTrace();
                    handlerPost(context, mHandler, new HttpThreadHandler().new HandlerPostCallback() {
                        @Override
                        public void postCallback() {
                            httpCallBack.runCallback(HttpThreadHandler.URL_MALFORMED_ERROR, new ArrayMap<String,String>(), "", "MalformedURLException");
                        }
                    });
                } catch (ProtocolException pe) {
                    pe.printStackTrace();
                    handlerPost(context, mHandler, new HttpThreadHandler().new HandlerPostCallback() {
                        @Override
                        public void postCallback() {
                            httpCallBack.runCallback(HttpThreadHandler.PROTOCAL_ERROR, new ArrayMap<String,String>(), "", "ProtocolException");
                        }
                    });
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    handlerPost(context, mHandler, new HttpThreadHandler().new HandlerPostCallback() {
                        @Override
                        public void postCallback() {
                            httpCallBack.runCallback(HttpThreadHandler.IO_ERROR, new ArrayMap<String,String>(), "", "IOException");
                        }
                    });
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        });
    }

    public static void put(final Context context, final String urlPut, final ArrayMap<String,String> headerProperties, final String dataPut, final HttpThreadCallback httpCallBack) {

        threadRun(context, new HttpThreadHandler().new ThreadRunCallback() {
            @Override
            public void runCallback() {

                HttpURLConnection conn = null;
                try {
                    // 建立連線
                    URL url = new URL(urlPut);
                    trustAllHosts();
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("PUT");
                    conn.setRequestProperty("Content-Type", "application/json");
                    if (headerProperties != null) {
                        for (int i=0;i<headerProperties.size();i++) {
                            conn.setRequestProperty(headerProperties.keyAt(i), headerProperties.valueAt(i));
                        }
                    }
                    conn.setRequestProperty("Content-Length", String.valueOf(dataPut.getBytes().length));
                    conn.setDoOutput(true); // 開啟上傳資料
                    conn.setDoInput(true); // 開啟接收資料
                    OutputStream os = conn.getOutputStream();
                    os.write(dataPut.getBytes("UTF-8"));
                    os.flush();
                    os.close();
                    int runStatus = conn.getResponseCode();
                    final ArrayMap<String,String> responseHeaderProperties = new ArrayMap<>();
                    for (int i=0;i<conn.getHeaderFields().size();i++) {
                        responseHeaderProperties.put(conn.getHeaderFieldKey(i),conn.getHeaderField(i));
                    }
                    if (runStatus == 200) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        reader.close();
                        final String result = sb.toString();
                        handlerPost(context, mHandler, new HttpThreadHandler().new HandlerPostCallback() {
                            @Override
                            public void postCallback() {
                                httpCallBack.runCallback(HttpThreadHandler.HTTP_SUCCESS, responseHeaderProperties, result, "");
                            }
                        });
                    } else if (runStatus == 404) {
                        handlerPost(context, mHandler, new HttpThreadHandler().new HandlerPostCallback() {
                            @Override
                            public void postCallback() {
                                httpCallBack.runCallback(HttpThreadHandler.URL_NOT_FOUND, responseHeaderProperties, "", "UrlNotFoundException");
                            }
                        });
                    } else if (runStatus == 490) {
                        if (conn.getHeaderField("error_code") != null) {
                            final String errorCode = conn.getHeaderField("error_code");
                            handlerPost(context, mHandler, new HttpThreadHandler().new HandlerPostCallback() {
                                @Override
                                public void postCallback() {
                                    httpCallBack.runCallback(HttpThreadHandler.SERVER_ERROR, responseHeaderProperties, "", errorCode);
                                }
                            });
                        } else {
                            handlerPost(context, mHandler, new HttpThreadHandler().new HandlerPostCallback() {
                                @Override
                                public void postCallback() {
                                    httpCallBack.runCallback(HttpThreadHandler.SERVER_ERROR, responseHeaderProperties, "", "ServerProcessException");
                                }
                            });
                        }
                    } else if (runStatus >= 400 && runStatus < 600) {
                        handlerPost(context, mHandler, new HttpThreadHandler().new HandlerPostCallback() {
                            @Override
                            public void postCallback() {
                                httpCallBack.runCallback(HttpThreadHandler.NETWORK_PROCESS_ERROR, responseHeaderProperties, "", "NetworkProcessException");
                            }
                        });
                    }
                } catch (MalformedURLException mue) {
                    mue.printStackTrace();
                    handlerPost(context, mHandler, new HttpThreadHandler().new HandlerPostCallback() {
                        @Override
                        public void postCallback() {
                            httpCallBack.runCallback(HttpThreadHandler.URL_MALFORMED_ERROR, new ArrayMap<String,String>(), "", "MalformedURLException");
                        }
                    });
                } catch (ProtocolException pe) {
                    pe.printStackTrace();
                    handlerPost(context, mHandler, new HttpThreadHandler().new HandlerPostCallback() {
                        @Override
                        public void postCallback() {
                            httpCallBack.runCallback(HttpThreadHandler.PROTOCAL_ERROR, new ArrayMap<String,String>(), "", "ProtocolException");
                        }
                    });
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    handlerPost(context, mHandler, new HttpThreadHandler().new HandlerPostCallback() {
                        @Override
                        public void postCallback() {
                            httpCallBack.runCallback(HttpThreadHandler.IO_ERROR, new ArrayMap<String,String>(), "", "IOException");
                        }
                    });
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }

            }
        });

    }


    public static void delete(final Context context, final String urlDelete, final ArrayMap<String,String> headerProperties, final HttpThreadCallback httpCallBack) {

        threadRun(context, new HttpThreadHandler().new ThreadRunCallback() {
            @Override
            public void runCallback() {

                HttpURLConnection conn = null;
                try {
                    // 建立連線
                    URL url = new URL(urlDelete);
                    trustAllHosts();
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("DELETE");
                    conn.setRequestProperty("Content-Type", "application/json");
                    if (headerProperties != null) {
                        for (int i=0;i<headerProperties.size();i++) {
                            conn.setRequestProperty(headerProperties.keyAt(i), headerProperties.valueAt(i));
                        }
                    }
                    conn.setRequestProperty("Content-Length", "0");
                    conn.setDoInput(true); // 開啟接收資料
                    int runStatus = conn.getResponseCode();
                    final ArrayMap<String,String> responseHeaderProperties = new ArrayMap<>();
                    for (int i=0;i<conn.getHeaderFields().size();i++) {
                        responseHeaderProperties.put(conn.getHeaderFieldKey(i),conn.getHeaderField(i));
                    }
                    if (runStatus == 200) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        reader.close();
                        final String result = sb.toString();
                        handlerPost(context, mHandler, new HttpThreadHandler().new HandlerPostCallback() {
                            @Override
                            public void postCallback() {
                                httpCallBack.runCallback(HttpThreadHandler.HTTP_SUCCESS, responseHeaderProperties, result, "");
                            }
                        });
                    } else if (runStatus == 404) {
                        handlerPost(context, mHandler, new HttpThreadHandler().new HandlerPostCallback() {
                            @Override
                            public void postCallback() {
                                httpCallBack.runCallback(HttpThreadHandler.URL_NOT_FOUND, responseHeaderProperties, "", "UrlNotFoundException");
                            }
                        });
                    } else if (runStatus == 490) {
                        if (conn.getHeaderField("error_code") != null) {
                            final String errorCode = conn.getHeaderField("error_code");
                            handlerPost(context, mHandler, new HttpThreadHandler().new HandlerPostCallback() {
                                @Override
                                public void postCallback() {
                                    httpCallBack.runCallback(HttpThreadHandler.SERVER_ERROR, responseHeaderProperties, "", errorCode);
                                }
                            });
                        } else {
                            handlerPost(context, mHandler, new HttpThreadHandler().new HandlerPostCallback() {
                                @Override
                                public void postCallback() {
                                    httpCallBack.runCallback(HttpThreadHandler.SERVER_ERROR, responseHeaderProperties, "", "ServerProcessException");
                                }
                            });
                        }
                    } else if (runStatus >= 400 && runStatus < 600) {
                        handlerPost(context, mHandler, new HttpThreadHandler().new HandlerPostCallback() {
                            @Override
                            public void postCallback() {
                                httpCallBack.runCallback(HttpThreadHandler.NETWORK_PROCESS_ERROR, responseHeaderProperties, "", "NetworkProcessException");
                            }
                        });
                    }
                } catch (MalformedURLException mue) {
                    mue.printStackTrace();
                    handlerPost(context, mHandler, new HttpThreadHandler().new HandlerPostCallback() {
                        @Override
                        public void postCallback() {
                            httpCallBack.runCallback(HttpThreadHandler.URL_MALFORMED_ERROR, new ArrayMap<String,String>(), "", "MalformedURLException");
                        }
                    });
                } catch (ProtocolException pe) {
                    pe.printStackTrace();
                    handlerPost(context, mHandler, new HttpThreadHandler().new HandlerPostCallback() {
                        @Override
                        public void postCallback() {
                            httpCallBack.runCallback(HttpThreadHandler.PROTOCAL_ERROR, new ArrayMap<String,String>(), "", "ProtocolException");
                        }
                    });
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    handlerPost(context, mHandler, new HttpThreadHandler().new HandlerPostCallback() {
                        @Override
                        public void postCallback() {
                            httpCallBack.runCallback(HttpThreadHandler.IO_ERROR, new ArrayMap<String,String>(), "", "IOException");
                        }
                    });
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }

            }
        });
    }

    private static void trustAllHosts() {
        final String TAG = "trustAllHosts";
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                Log.i(TAG, "checkClientTrusted");
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                Log.i(TAG, "checkServerTrusted");
            }
        } };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}