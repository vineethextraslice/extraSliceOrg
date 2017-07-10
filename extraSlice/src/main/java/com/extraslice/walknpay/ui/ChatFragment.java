package com.extraslice.walknpay.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.app.extraslice.R;
import com.extraslice.walknpay.bl.CustSupportBO;
import com.extraslice.walknpay.bl.Utilities;
import com.extraslice.walknpay.dao.ProgressClass;
import com.extraslice.walknpay.model.ChatMessage;
import com.extraslice.walknpay.model.SupportOfficerModel;

//import org.jivesoftware.smack.XMPPConnection;

public class ChatFragment extends Fragment {
	private static final String ASSIGN ="Assign";
	private static final String UPDATE ="Update";
	private static final String CHAT ="Chat";
	String sender="";
	String receiver="";
	String senderEmail="";
	String receiverEmail="";
	String senderPassword="";
	String errorMessage = null;
	LinearLayout errorLyt,supportOffcrNameLyt;
	TextView errorView,busyText;
	Context mContext;
	public static List<ChatMessage> messages = new ArrayList<ChatMessage>();
	private Handler mHandler = new Handler();

	private EditText mSendText;
	private TableLayout listMessages;
	public static SupportOfficerModel userModel;
	private static XMPPConnection connection;
	View rootView;
	ImageView errorImage;
	ConnectionDetector cd;
	Button tryagain ;
	TextView supportOffcrName;
	//ChatListAdaptor adapter ;
	public ChatFragment() {
		
	}
	public ChatFragment(String sender, String senderPassword) {

		if (sender.indexOf("@") > 0) {
			this.senderEmail=sender.substring(0, sender.indexOf("@"));
			this.sender = sender.replaceAll("@", "_");
			
		} else {
			this.sender = sender;
		}
		this.senderPassword = senderPassword;
	}

	/**
	 * Called with the activity is first created.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Utilities.currentTab = "CHAT";
		mContext = getActivity();
		cd = new ConnectionDetector(getActivity());
		rootView = inflater.inflate(R.layout.chat_screen, container, false);
		errorLyt = (LinearLayout) rootView.findViewById(R.id.errorLyt);
		errorView = (TextView) rootView.findViewById(R.id.errorText);
		busyText = (TextView) rootView.findViewById(R.id.busyText);
		errorImage= (ImageView) rootView.findViewById(R.id.errorImage);
		mSendText = (EditText) rootView.findViewById(R.id.sendText);
		tryagain = (Button) rootView.findViewById(R.id.tryagain);
		supportOffcrName = (TextView) rootView.findViewById(R.id.supportOffcrName);
		supportOffcrNameLyt = (LinearLayout) rootView.findViewById(R.id.supportOffcrNameLyt);
		if (cd.isConnectingToInternet()) {
			new LoadData(ASSIGN).execute();
		} else {
			setErrorMessage("No connectivity");
			connection = null;
		}
		setConnection(connection);
		mSendText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Utilities.showVirtualKeyBoard(getActivity(), mSendText);
			}
		});
		listMessages = (TableLayout) rootView.findViewById(R.id.listMessages);
	

		setChatMessage();
		//setListAdapter();
		tryagain.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Fragment fragment = null;
				fragment = new ChatFragment(sender, senderPassword);
				if (fragment != null) {
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
				}
			}
		});
		Button exit = (Button) rootView.findViewById(R.id.exit);
		exit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				new LoadData(UPDATE).execute();
			}
		});
		
		Button clear = (Button) rootView.findViewById(R.id.clear);
		clear.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				messages.clear();
				mHandler.post(new Runnable() {
					public void run() {
						setListAdapter();
					}
				});
			}
		});
		
		// Set a listener to send a chat text message
		ImageButton send = (ImageButton) rootView.findViewById(R.id.send);

		send.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				
				String text = mSendText.getText().toString();
				if (text.trim().isEmpty()) {
					return;
				}
				Utilities.hideVirtualKeyBoard(getActivity(), mSendText);
				if ((connection == null || !connection.isConnected()) && cd.isConnectingToInternet()) {
					try {
						connection = new InitiateXMPPConnection().execute().get();
					} catch (InterruptedException e) {
						connection = null;
						e.printStackTrace();
					} catch (ExecutionException e) {
						connection = null;
						e.printStackTrace();
					}
					setConnection(connection);
				}
				if (connection != null && connection.isConnected() && cd.isConnectingToInternet() && receiver != null) {
					String to = receiver;
					mSendText.setText("");
					Message msg = new Message(to, Message.Type.chat);
					msg.setBody(text);
					// connection.sendPacket(msg);
					Chat chat = connection.getChatManager().createChat(to, new MessageListener() {
						@Override
						public void processMessage(Chat chat, Message message) {

						}
					});

					try {
						chat.sendMessage(text);
						new LoadData(CHAT).execute();
					} catch (XMPPException e) {
					}
					// chat.sendMessage(message);
					ChatMessage chtMsg = new ChatMessage();
					chtMsg.setMessage(text);
					chtMsg.setUserName(connection.getUser());
					chtMsg.setUserType(1);
					messages.add(chtMsg);
					// messages.add(text);
					setListAdapter();
				} else {
					if (cd.isConnectingToInternet()) {
						setErrorMessage("No Connectivity");
					} else if (connection != null && connection.isConnected()) {
						setErrorMessage("Could not connect to chat server");
					} else if (receiver == null) {
						setErrorMessage("All support officers are busy");
					}
				}
			}
		});

		return rootView;
	}

	/**
	 * Called by Settings dialog when a connection is establised with the XMPP
	 * server
	 * 
	 * @param connection
	 */
	public void setConnection(XMPPConnection conn) {
		connection = conn;
		if (connection != null) {
			// Add a packet listener to get messages sent to us
			PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
			connection.addPacketListener(new PacketListener() {
				public void processPacket(Packet packet) {
					Message message = (Message) packet;
					if (message.getBody() != null) {
						String fromName = StringUtils.parseBareAddress(message.getFrom());
						ChatMessage chtMsg = new ChatMessage();
						chtMsg.setMessage(message.getBody());
						chtMsg.setUserName(fromName);
						chtMsg.setUserType(2);
						messages.add(chtMsg);

						// Add the incoming message to the list view
						mHandler.post(new Runnable() {
							public void run() {
								setListAdapter();
							}
						});
					}
				}
			}, filter);
		}
	}

	private void setListAdapter() {	
		/*adapter = new ChatListAdaptor(rootView.getContext(), R.layout.chat_message_list, messages,this.senderEmail,this.receiverEmail);
		mList.setAdapter(adapter);
		adapter.notifyDataSetChanged();*/
		setChatMessage();
	}

	public class InitiateXMPPConnection extends AsyncTask<Void, Void, XMPPConnection> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected XMPPConnection doInBackground(Void... params) {
			XMPPConnection conn = null;
			if (userModel != null) {
				int port = userModel.getChatServerPort();// getText(R.id.port);
				String service = userModel.getChatServerService();// "talkative.com";//getText(R.id.service);
				// Create a connection
				ConnectionConfiguration connConfig = new ConnectionConfiguration(userModel.getChatServerIP(), port, service);
				conn = new XMPPConnection(connConfig);// MPPConnection(connConfig);

				try {
					conn.connect();

				} catch (XMPPException ex) {
					setErrorMessage("Could not connect chat server");
					conn = null;
				}
				try {
					if (conn != null) {
						conn.login(sender, senderPassword);
						// Set the status to available
						Presence presence = new Presence(Presence.Type.available);
						conn.sendPacket(presence);
					}
				} catch (XMPPException ex) {
					setErrorMessage("chat server login failed");
					conn = null;
				}
			} else {
				setErrorMessage("Could not connect chat server");
			}
			return conn;

		}

	}
	private void  setErrorMessage(String error){
		setErrorMessage(error, true);
	}

	private void setErrorMessage(String error, boolean showTryAgain) {
		try {
			if (errorView == null) {
				return;
			}
			if (error != null && error.trim().length() > 0) {
				errorView.setText(error);
				errorLyt.setVisibility(View.VISIBLE);
				errorView.setTextColor(Color.RED);
				if (tryagain != null) {
					if (showTryAgain) {
						tryagain.setVisibility(View.VISIBLE);
						errorImage.setVisibility(View.VISIBLE);
						supportOffcrName.setVisibility(View.GONE);
						supportOffcrNameLyt.setVisibility(View.GONE);
					} else {
						tryagain.setVisibility(View.GONE);
						errorImage.setVisibility(View.GONE);
						errorView.setTextColor(Color.BLUE);
					}
				}
			} else {
				errorView.setText("");
				errorLyt.setVisibility(View.GONE);
				if (tryagain != null) {
					tryagain.setVisibility(View.GONE);
					errorImage.setVisibility(View.GONE);
				}
			}
		} catch (Exception e) {

		}

	}
	
	class LoadData extends AsyncTask<Void, Void, Void> {
		String process;
		CustSupportBO custSupportBO;
		public LoadData(String process) {
			this.process=process;
		}
		@Override
		protected void onPreExecute() {	
			ProgressClass.startProgress(mContext);
			 custSupportBO = new CustSupportBO(mContext);
			super.onPreExecute();

		}
		@Override
		protected Void doInBackground(Void... arg0) {
			if(this.process.equalsIgnoreCase(ASSIGN)){
				userModel = custSupportBO.assignAvailableOfficer(Utilities.loggedInUser.getUserId());
			}else if(this.process.equalsIgnoreCase(UPDATE)){
				if (userModel != null) {
					custSupportBO.updateOfficerAvailable(userModel.getOfficerId());
				}
			}else if(this.process.equalsIgnoreCase(CHAT)){
				custSupportBO.updateLastSupportTime(userModel.getOfficerId(), Utilities.loggedInUser.getUserId(), userModel.getChatId());
			}
			
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			if(this.process.equalsIgnoreCase(ASSIGN)){
				if (userModel != null) {
					receiver = userModel.getOfficerUserName();
					receiverEmail=receiver.substring(0, receiver.lastIndexOf("@"));
					supportOffcrName.setText("You are connected to : "+receiverEmail);
					supportOffcrName.setVisibility(View.VISIBLE);
					supportOffcrNameLyt.setVisibility(View.VISIBLE);
					if(userModel!= null && !userModel.isAvailable()){
						busyText.setText(receiverEmail+" is busy, response may be delayed");
						busyText.setVisibility(View.VISIBLE);
						
					}
					
					try {
						connection = new InitiateXMPPConnection().execute().get();
						setConnection(connection);
					} catch (InterruptedException e) {
						setErrorMessage(e.getLocalizedMessage());
						receiver = null;
					} catch (ExecutionException e) {
						setErrorMessage(e.getLocalizedMessage());
						receiver = null;
					}
				} else {
					setErrorMessage(Utilities.errorMessagenew);
					receiver = null;
				}
			}else if(this.process.equalsIgnoreCase(UPDATE)){
				Fragment fragment = null;
				fragment = new HomeFragment();
				if (fragment != null) {
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
				}
			}
			super.onPostExecute(result);
			ProgressClass.finishProgress();
		}
	}
	
	private void setChatMessage(){
		listMessages.removeAllViews();
		
		for(ChatMessage chatMessage :messages){
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View convertView = inflater.inflate( R.layout.chat_message_list, null);
			TextView chatText = (TextView) convertView.findViewById(R.id.chatText);
			ImageView ccare_head = (ImageView) convertView.findViewById(R.id.ccare_head);
			ImageView customer_head = (ImageView) convertView.findViewById(R.id.customer_head);
			LinearLayout chat_lyt = (LinearLayout) convertView.findViewById(R.id.chat_lyt);
			LinearLayout text_lyt = (LinearLayout) convertView.findViewById(R.id.text_lyt);
			if (chatMessage.getUserType() == 1) {
				chat_lyt.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
				text_lyt.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);				
				chatText.setBackgroundResource(R.drawable.oval_background);
				customer_head.setVisibility(View.VISIBLE);
				ccare_head.setVisibility(View.GONE);
				chatText.setVisibility(View.VISIBLE);
				chatText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
			} else {
				chat_lyt.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
				text_lyt.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
				chatText.setBackgroundResource(R.drawable.oval_background_grey);
				customer_head.setVisibility(View.GONE);
				ccare_head.setVisibility(View.VISIBLE);
				chatText.setVisibility(View.VISIBLE);
				chatText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
			}
			chatText.setText(chatMessage.getMessage());
			listMessages.addView(convertView);
		}
		
	}

}
