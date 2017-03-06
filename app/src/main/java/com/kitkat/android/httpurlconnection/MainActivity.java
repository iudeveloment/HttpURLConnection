package com.kitkat.android.httpurlconnection;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/** HttpURLConnection
 *
 *      Web Networking with HTTP Protocol (HTML WebPage Data)
 *      Client (Android) To Web Server Connection
 *
 *      RxJava, Firebase, okHttp, Tomcat.. 의 기초
 *
 *      구형 디바이스에서는 데이터 통신 속도가 느려서, Socket (IP + PORT) 연결 또는
 *      비연결성 (Stateless) HTTP Protocol 요청은 내부적으로 Socket 연결을 새로 하고 응답 후 닫히므로,
 *      대기 시간이 소모. 최근의 디바이스 성능 향상, 무선 네트워크 환경 향상으로 HTTP Protocol 기반의 Data 통신 향상
 *
 *      ! HttpURLConnection 은 인터넷이 필요하므로 AndroidManifest.xml 상에
 *        <uses-permission android:name="android.permission.INTERNET"/> 권한 필요
 *
 * URL (Uniform Resource Locator)
 *
 *      인터넷 상의 수 많은 정보가 위치하는 유니크한 위치 정보
 *      전세계적으로 중복되지 않는 유일무이한 식별자
 *
 *      [Scheme]://[Hosts]:[Port][/Url-Path][?Query][#Bookmark]
 *
 *      [Scheme]    Web 통신에 사용되는 방식, 프로토콜 (디지털 정보 통신 시 정보체계 및 정의)
 *                  Scheme 뒤의 Url-Path 상의 데이터가 어떤 통신규약에 따라 주고 받아져야 하는지 규정
 *
 *      [Host]      자원이 위치하고 있는 컴퓨터의 식별자
 *                  - IP (Internet Protocol)  인터넷 TCP/IP Protocol 사용을 위한 주소
 *                  - Domain Address          IP 주소를 사람이 식별하기 쉽도록 문자 등으로 변경한 주소
 *
 *      [Url-Path]  Host 내의 Root Directory ( / )로 부터 자원이 위치한 장소까지의 경로와 자원의 파일명
 *
 *      [Query]     Web Server 에게 전달하는 추가적인 질문, Parameter
 *                  Query 값에 따라 차등된 다른 데이터, HTML WebPage 출력
 *
 *      [Bookmark]  어떤 WebPage 문서 안에서 북마크를 통해서 특정한 지점으로 하이퍼링크, 스크롤 기능
 *
 *  Java URL Class
 *
 *      URL Class 는 World Wide Web 상의 리소스 (자원)에 대한 URL 관련 Class.
 *      리소스는 파일이나 디렉토리처럼 간단 할 수도 있고 데이터베이스 나 검색 엔진에 대한 쿼리와 같이 더 복잡한 객체에 대한 참조 일 수도 있다
 *
 *      public URLConnection openConnection()   return URLConnection Instance from URL String spec or String Protocol or URL context
 *                                              URL 객체의 URL String 이 "http://" or "https://" 프로토콜을 포함하면  HttpURLConnection 객체를 만들게 되므로
 *                                              (HttpURLConnection)으로 Type Casting 하여 연결
 *                                              (HttpURLConnection 객체는 HTTP 요청 방식 및 요청 파라미터 설정 가능)
 *
 *  HttpURLConnection API (java.net)
 *
 *      Web Networking Class with HTTP Protocol (HTML WebPage Data)
 *      Client (Android) To Web Server Connection Class
 *
 *      public void setRequestMethod(String method)                     HTTP Protocol 의 HTTP method (요청방식) 지정 Method
 *      public void setRequestProperty(String field, String newValue)   Request Header Message 의 필드값 지정 Method
 *                                                                      (웹 브라우저가 웹 서버에게 HTML 요청 시 작성한 요청서)
 *      public void setConnectionTimeout()                              연결 대기 시간 설정
 *      public void setDoInput()                                        HttpURLConnection 객체의 입력 허용
 *      public void setDoOutput()                                       HttpURLConnection 객체의 출력 허용
 *
 *      public int getResponseCode()                                   Request To Web Server, return Response Code
 *                                                                     이 시점에서 내부적으로 웹 서버에 연결, HTML WebPage 요청 과정 및 전송 진행
 *
 *      public InputStream getInputStream()                            HttpURLConnection 으로 부터 응답받은 HTTP WebPage Data 읽기의 InputStream 객체 반환
 *      public OutputStream getOutputStream()                          HttpURLConnection 으로 부터 웹 서버에 전송할 Data 쓰기의 OutputStream 객체 반환
 *
 *  HTTP Method (요청방식)
 *
 *      웹 클라이언트와 웹 서버가 상호작용하는 방식
 *      웹 클라이언트가 웹 서버에 요청의 목적/종류를 알리는 메소드
 *
 *      GET 방식      웹 클라이언트가 웹 서버에게 해당 URL 의 정보를 요청해서 가져오는 방식
 *                    URL 주소에 Query String 이 보이는 방식
 *
 *      POST 방식     웹 클라이언트의 정보를 웹 서버에게 전달(입력) 하는 방식
 *                    URL 주소에 Query String, Request Data 을 감추는 방식
 *
 *      PUT 방식      웹 클라이언트의 정보를 웹 서버에게 전달하는 방식 (정보 수정 시!)
 *      DELETE 방식   웹 클라이언트가 웹 서버에 해당 URL 의 정보 삭제 요청
 *
 *  NetworkOnMainThreadException - HttpURLConnection & Network IO
 *      HttpURLConnection 과 같은 Network IO 는 Main Thread 에서 Task 처리 불가!
 *      반드시, Sub Thread 와 Handler 또는 AsyncTask 로 처리
 *
 *      AsyncTask 시작과 끝이 정해진 Task 인 Network IO 작업에 적합
 */
public class MainActivity extends AppCompatActivity {
    private EditText editUrl;
    private TextView textTitle;
    private TextView textHtml;
    private Button btnConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    /** Web Connection with HTTP Protocol (HTML WebPage) Method
     *
     *  Client (Android) To Web Server Connection
     *  URL 주소로 해당 Web Server (Google, Naver..) 접속 후 WebPage HTML Code 요청하기
     *
     * @param webUrlString URL String for URL Class Instantiate
     */
    public String getHtml(String webUrlString) {
        // HttpURLConnection 객체를 만들기 위해서는 URL String 이 "http://" or "https://" 프로토콜 포함
        if(!webUrlString.startsWith("http://"))
            webUrlString = "http://" + webUrlString;

        // 9. HttpURLConnection 과 같은 Network IO 는 Main Thread 에서 Task 처리 불가!
        // 반드시, Sub Thread 와 Handler 또는 AsyncTask 로 처리
        new AsyncTask<String, Void, String>() {
            ProgressDialog dialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                // ProgressDialog 처리
                dialog = new ProgressDialog(MainActivity.this);
                dialog.setMessage("HttpURLConnection..");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.show();
            }

            @Override
            protected String doInBackground(String... params) {
                try {

                    // 1. Web Server URL String 으로 URL 객체 생성
                    URL url = new URL(params[0]);

                    // 2. URL 객체의 openConnection() Method 로 URLConnection 객체 생성
                    // (HttpURLConnection) Type Casting
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                    // 3, HTTP 프로토콜에 대한 HTTP Method (요청 방식) 지정
                    httpURLConnection.setRequestMethod("GET");

                    // 4. HTTPUrlConnection Setting
                    httpURLConnection.setConnectTimeout(1000); // ms
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);

                    // 5. 웹 서버에 요청 및 응답 코드 회신
                    // 이 시점에서 내부적으로 웹 서버에 연결, HTML WebPage 요청 과정 및 전송 진행
                    int responseCode = httpURLConnection.getResponseCode();

                    // 6. 응답 코드에 따른 처리
                    // HTTP 응답 코드는 HttpUrlConnection Class 내에 상수로 정의
                    if(responseCode == HttpURLConnection.HTTP_OK) {
                        // HTTP 처리 OK

                        // 7. 응답받은 HTTP WebPage Data 를 HttpURLConnection 의 Stream 으로 읽어오기
                        // readLine() 으로 한 줄씩 HTTP WebPage 의 Text 를 String 으로 한 줄씩 읽어오기 위해 InputStreamReader -> BufferedReader 로 Wrapping
                        BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                        // 8. 반복문을 돌며 BufferedReader Data 읽기
                        StringBuilder sb = new StringBuilder();
                        String dataLine = null;

                        while ((dataLine = br.readLine()) != null) {
                            sb.append(dataLine);
                        }

                        return sb.toString();

                    } else {
                        // HTTP 처리 ERROR
                        Log.e("HttpUrlConnection", "Error Code" + responseCode);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                dialog.dismiss();
                textTitle.setText(parsing(result));
                textHtml.setText(result);
            }
        }.execute(webUrlString);

        return null;
    }

    public void init() {
        editUrl = (EditText) findViewById(R.id.editUrl);
        textTitle = (TextView) findViewById(R.id.textTitle);
        textHtml = (TextView) findViewById(R.id.textHtml);

        btnConnect = (Button) findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlString = editUrl.getText().toString();
                getHtml(urlString);
            }
        });
    }

    public String parsing(String str) {
        String token = "";

        if(str != null)
            token = str.substring(str.indexOf("<title>") + 7, str.indexOf("</title>"));

        return token;
    }
}
