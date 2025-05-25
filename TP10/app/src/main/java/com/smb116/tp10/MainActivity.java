package com.smb116.tp10;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NsdManager mNsdManager;
    private ServerSocket mServerSocket;
    private MyRunnableService run;
    private TextView afficheService, afficheDisc;
    private Button startService, stopService, startDisc, stopDisc, msgDisc, bntFin;
    private String SERVICE_NAME = "Deptinfo";
    private String SERVICE_TYPE = "_http._tcp";
    public String serviceLog = "";
    private String discoveringLog = "";
    private String msgSend;
    private boolean ecouteSocket = false;
    private boolean discoveringOn = false;
    private static final int MLOCALPORT = 39327;
    static final int MSGSERVICE = 1;
    static final int MSGDISC = 2;
    static final int MSGCO = 3;
    static final int MSGDECO = 4;
    private List<NsdServiceInfo> addrList = new ArrayList<>();

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            String value = String.valueOf(msg.obj);
            if(msg.what == MSGSERVICE) {
                serviceLog +=  value + "\n";
                afficheService.setText(serviceLog);
            }else if(msg.what == MSGDISC) {
                discoveringLog += value + "\n";
                afficheDisc.setText(discoveringLog);
            }else if(msg.what == MSGCO)  {
                Bundle data = (Bundle)msg.obj;
                NsdServiceInfo serviceInfo = data.getParcelable("service");
                addrList.add(serviceInfo);
            }else if(msg.what == MSGDECO) {
                Iterator<NsdServiceInfo> infoIterator = addrList.iterator();
                while (infoIterator.hasNext()) {
                    if(infoIterator.next().getServiceName().equals(value)) {
                        infoIterator.remove();
                    }
                }
            }
        }
    };

    NsdManager.RegistrationListener registrationListener = new NsdManager.RegistrationListener() {
        @Override
        public void onServiceRegistered(NsdServiceInfo nsdServiceInfo) {
            // Le nom final peut être différent si des doublons existent
            String registeredName = nsdServiceInfo.getServiceName();
            Log.e("NSD", "Service enregistré : " + registeredName + " " + String.valueOf(nsdServiceInfo.getPort()));
            Message msg = handler.obtainMessage(MSGSERVICE, "onServiceRegistered: " + registeredName);
            handler.sendMessage(msg);
        }

        @Override
        public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
            Log.e("NSD", "Échec de l'enregistrement : " + errorCode);
            Message msg = handler.obtainMessage(MSGSERVICE, "onRegistrationFailed: " + String.valueOf(errorCode));
            handler.sendMessage(msg);
        }

        @Override
        public void onServiceUnregistered(NsdServiceInfo serviceInfo) {
            Log.e("NSD", "Service supprimé : " + serviceInfo.getServiceName());
            Message msg = handler.obtainMessage(MSGSERVICE, "onServiceUnregistered");
            handler.sendMessage(msg);
        }

        @Override
        public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
            Log.e("NSD", "Échec de la suppression du service : " + errorCode);
            Message msg = handler.obtainMessage(MSGSERVICE, "onUnregistrationFailed");
            handler.sendMessage(msg);
        }
    };

    NsdManager.DiscoveryListener discoveryListener = new NsdManager.DiscoveryListener() {
        @Override
        public void onStartDiscoveryFailed(String s, int i) {

        }

        @Override
        public void onStopDiscoveryFailed(String s, int i) {
            Message msg = handler.obtainMessage(MSGDISC, "onStopDiscoveryFailed");
            handler.sendMessage(msg);
        }

        @Override
        public void onDiscoveryStarted(String s) {
            Message msg = handler.obtainMessage(MSGDISC, "onDiscoveryStarted");
            handler.sendMessage(msg);
        }

        @Override
        public void onDiscoveryStopped(String s) {
            Message msg = handler.obtainMessage(MSGDISC, "onDiscoveryStopped");
            handler.sendMessage(msg);
        }

        @Override
        public void onServiceFound(NsdServiceInfo nsdServiceInfo) {
            Message msg = handler.obtainMessage(MSGDISC, "onServiceFound: "
                    + nsdServiceInfo.getServiceName() + "(" + nsdServiceInfo.getServiceType() + ")");
            handler.sendMessage(msg);
            NsdManager.ResolveListener mResolveListener = new NsdManager.ResolveListener() {
                public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {

                }
                public void onServiceResolved(NsdServiceInfo serviceInfo) {
                    String name = serviceInfo.getServiceName();
                    int port = serviceInfo.getPort();
                    String address = serviceInfo.getHost().getHostAddress();
                    Message msg = handler.obtainMessage(MSGDISC, "onServiceResolved: "
                            + name + "(port: " + String.valueOf(port) + ", adresse:" + address + ")");
                    handler.sendMessage(msg);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("service", serviceInfo);
                    msg = handler.obtainMessage(MSGCO, bundle);
                    handler.sendMessage(msg);
                }
            };
            mNsdManager.resolveService(nsdServiceInfo, mResolveListener);
        }

        @Override
        public void onServiceLost(NsdServiceInfo nsdServiceInfo) {
            Message msg = handler.obtainMessage(MSGDISC, "onServiceLost");
            handler.sendMessage(msg);
            msg = handler.obtainMessage(MSGDECO, nsdServiceInfo.getServiceName());
            handler.sendMessage(msg);
        }
    };

    public void startNDS() {
        if(!ecouteSocket) {
            Log.e("NSD", "Start service ");
            // Initialize a server socket on the next available port.
            try {
                mServerSocket = new ServerSocket(MLOCALPORT);
            } catch (IOException e) {
                Log.e("NSD", "NSD Service Error:\n" + e.toString());
                serviceLog += "NSD Service Error:\n" + e.toString();
            }
            // Create the NsdServiceInfo object, and populate it.
            NsdServiceInfo serviceInfo = new NsdServiceInfo();
            // The name is subject to change based on conflicts with other services advertised on the same network
            serviceInfo.setServiceName(SERVICE_NAME);
            serviceInfo.setServiceType(SERVICE_TYPE);
            serviceInfo.setPort(MLOCALPORT);
            serviceLog += "NSD Service started (" + String.valueOf(MLOCALPORT) + ")...\n";
            afficheService.setText(serviceLog);
            mNsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener);
            ecouteSocket = true;
            run = new MyRunnableService();
            new Thread(run).start();
        }
    }

    public void stopNDS() throws IOException {
        if(ecouteSocket){
            mServerSocket.close();
            ecouteSocket = false;
            mNsdManager.unregisterService(registrationListener);
            serviceLog += "MyRunnableService stopped...\n";
            serviceLog += "NSD Service stoped\n";
            afficheService.setText(serviceLog);
        }
    }

    public void startDiscorvering() {
        if(!discoveringOn) {
            discoveringOn = true;
            discoveringLog += "Discovering started...\n";
            afficheDisc.setText(discoveringLog);
            mNsdManager.discoverServices("_http._tcp", NsdManager.PROTOCOL_DNS_SD, discoveryListener);
        }
    }

    public void stopDiscorvering() {
        if(discoveringOn) {
            discoveringOn = false;
            discoveringLog += "Discovering stopped...\n";
            afficheDisc.setText(discoveringLog);
            mNsdManager.stopServiceDiscovery(discoveryListener);
        }
    }

    private void sendMessage() {
        if (discoveringOn && ecouteSocket) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Entrer un message");

            final EditText input = new EditText(MainActivity.this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    msgSend = input.getText().toString();
                    // Traitement du texte entré ici
                    Toast.makeText(MainActivity.this, "Entré : " + msgSend, Toast.LENGTH_SHORT).show();
                    sendMessageThread();
                }
            });
            builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
    }

    private void sendMessageThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (NsdServiceInfo addr : addrList) {
                    String serverIp = addr.getHost().getHostAddress();
                    int serverPort = addr.getPort();

                    try {
                        Message msg = handler.obtainMessage(MSGDISC, "connecting at " + serverIp + "...");
                        handler.sendMessage(msg);
                        Socket socket = new Socket(serverIp, serverPort);
                        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                        outputStream.writeUTF(msgSend);
                        outputStream.flush();
                        socket.setSoTimeout(300);
                        msg = handler.obtainMessage(MSGDISC, "sending message at " + serverIp + "...");
                        handler.sendMessage(msg);
                        DataInputStream in =
                                new DataInputStream(socket.getInputStream());
                        String rep = in.readUTF();
                        Log.d("thred message", rep);
                        if(rep.equals("OK")) {
                            msg = handler.obtainMessage(MSGDISC, "Message send at " + serverIp + ".");
                            handler.sendMessage(msg);
                        }
                        socket.shutdownInput();
                        socket.shutdownOutput();
                        socket.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }).start();
    }

    private void init() {
        mNsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);

        afficheService = (TextView) findViewById(R.id.afficheService);
        afficheDisc = (TextView) findViewById(R.id.afficheDisc);
        startService = (Button) findViewById(R.id.startService);
        stopService = (Button) findViewById(R.id.stopService);
        startDisc = (Button) findViewById(R.id.startDisc);
        stopDisc = (Button) findViewById(R.id.stopDisc);
        msgDisc = (Button) findViewById(R.id.msgDisc);
        stopService = (Button) findViewById(R.id.stopService);
        bntFin = (Button) findViewById(R.id.bntFin);

        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNDS();
            }
        });

        stopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ecouteSocket) {
                    try {
                        stopNDS();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        startDisc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDiscorvering();
            }
        });

        stopDisc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopDiscorvering();
            }
        });

        msgDisc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        bntFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        init();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private class MyRunnableService extends Thread {
        @Override
        public void run() {
            super.run();
            Message msg = handler.obtainMessage(MSGSERVICE, "Service running...");
            handler.sendMessage(msg);
            while (ecouteSocket) {
                Socket comSocket = null;
                try {
                    comSocket = mServerSocket.accept();
                    DataInputStream in =
                            new DataInputStream(comSocket.getInputStream());
                    msg = handler.obtainMessage(MSGSERVICE, "Message received from client: " + in.readUTF());
                    handler.sendMessage(msg);
                    DataOutputStream outputStream = new DataOutputStream(comSocket.getOutputStream());
                    outputStream.writeUTF("OK");
                    outputStream.flush();
                    in.close();
                    outputStream.close();

                } catch (IOException e) {
                    msg = handler.obtainMessage(MSGSERVICE, "MyRunnableService run error:\n" + e.toString());
                    handler.sendMessage(msg);
                }
                Log.v("socket server", "server socket fin cycle");
            }
        }

    }

}