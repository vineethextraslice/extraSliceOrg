package com.extraslice.walknpay.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.app.extraslice.R;
import com.extraslice.walknpay.bl.StoreBO;
import com.extraslice.walknpay.bl.Utilities;
import com.extraslice.walknpay.dao.ProgressClass;

public class WalletFragment extends Fragment implements OnTabChangeListener {

	private static final String TAG = "FragmentTabs";
	public static final String TAB_MYACCOUNT = "myaccount";
	public static final String TAB_RECEIPTS = "receipts";
	public static final String TAB_CARDS = "cards";

	private View mRoot;
	private TabHost mTabHost;
	private int mCurrentTab=0;
	int  rcptPerPage = 0;
	int pageSet = 0;
	int pageNo = 0;
	public WalletFragment(int tabNum) {
		mCurrentTab = tabNum;
	}
	public WalletFragment(int tabNum,int  rcptPerPage,int pageSet,int pageNo) {
		mCurrentTab = tabNum;
		this.rcptPerPage = rcptPerPage;
		this.pageSet = pageSet;
		this.pageNo = pageNo;
	}
	
	public WalletFragment()
	{
		mCurrentTab=0;
	}
		

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		mRoot = inflater.inflate(R.layout.walletfile2, null);
		mTabHost = (TabHost) mRoot.findViewById(android.R.id.tabhost);
		setupTabs();
		
		mTabHost.setCurrentTab(mCurrentTab);
		if(mCurrentTab==1)
			updateTab(TAB_CARDS, R.id.tab_2);
		else
			updateTab(TAB_RECEIPTS, R.id.tab_1);
		mTabHost.setOnTabChangedListener(this);
		return mRoot;
	}

/*	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		

	}*/

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void setupTabs() {
		mTabHost.setup(); // you must call this before adding your tabs!
		mTabHost.addTab(newTab(TAB_RECEIPTS, R.string.myreceipts, R.id.tab_1));
		mTabHost.addTab(newTab(TAB_CARDS, R.string.mycards, R.id.tab_2));
	}

	private TabSpec newTab(String tag, int labelId, int tabContentId) {

		View indicator = LayoutInflater.from(getActivity()).inflate(R.layout.tab, (ViewGroup) mRoot.findViewById(android.R.id.tabs), false);
		((TextView) indicator.findViewById(R.id.text)).setText(labelId);

		TabSpec tabSpec = mTabHost.newTabSpec(tag);
		tabSpec.setIndicator(indicator);
		tabSpec.setContent(tabContentId);
		return tabSpec;
	}

	@Override
	public void onTabChanged(String tabId) {
		if (TAB_RECEIPTS.equals(tabId)) {
			updateTab(tabId, R.id.tab_1);
			mCurrentTab = 0;
			return;
		}
		if (TAB_CARDS.equals(tabId)) {
			updateTab(tabId, R.id.tab_2);
			mCurrentTab = 1;
			return;
		}
	}

	private void updateTab(String tabId, int placeholder) {
		FragmentManager fm = getFragmentManager();
		if (tabId == TAB_RECEIPTS) {
			fm.beginTransaction().replace(placeholder, new MyReceipts(rcptPerPage,pageSet,pageNo)).commit();
		} else if (tabId == TAB_CARDS) {
			if(Utilities.selectedStore == null){
				new LoadData(placeholder).execute();
			}else{
				fm.beginTransaction().replace(placeholder, new MyCards()).commit();
			}
			
		}
	}

	
	
	class LoadData extends AsyncTask<Void, Void, Void> {
		int placeholder;
		public LoadData(int placeholder) {
			this.placeholder=placeholder;
		}
		
		@Override
		protected void onPreExecute() {	
			
			ProgressClass.startProgress(getActivity());
			
			super.onPreExecute();

		}
		@Override
		protected Void doInBackground(Void... arg0) {
			StoreBO storeBO = new StoreBO(getActivity());
			if(Utilities.storeList == null || Utilities.storeList.size() ==0){
				Utilities.storeList = storeBO.getAllStore(Utilities.STATUS_FILTER_ACTIVE,Utilities.userLatitude,Utilities.userLongitude);
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			if(Utilities.storeList != null && Utilities.storeList.size() == 1){
				Utilities.selectedStore = Utilities.storeList.get(0);
				FragmentManager fm = getFragmentManager();
				fm.beginTransaction().replace(placeholder, new MyCards()).commit();
			}else if (Utilities.storeList != null && Utilities.storeList.size() > 0) {
				Utilities.selectStorePopup(getActivity(), getFragmentManager(),new WalletFragment(1));
			} 
			super.onPostExecute(result);
			ProgressClass.finishProgress();
			FragmentManager fm = getFragmentManager();
			fm.beginTransaction().replace(placeholder, new MyCards()).commit();
		}
	}
}
