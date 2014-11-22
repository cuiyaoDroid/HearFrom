package com.tingshuo.hearfrom.screen;

import java.util.ArrayList;

import android.os.AsyncTask;

import com.tingshuo.hearfrom.HearFromApp;
import com.tingshuo.tool.L;
import com.tingshuo.tool.T;
import com.tingshuo.tool.db.MyCommitedMainPostListHelper;
import com.tingshuo.tool.db.mainPostListHelper;
import com.tingshuo.tool.db.mainPostListHolder;
import com.tingshuo.tool.view.pulltorefresh.PullToRefreshBase.Mode;
import com.tingshuo.web.http.HttpJsonTool;

public class mineSecondScreen extends mineTopicScreen{
	@Override
	protected void refreshList(final int maxId, final int minId, final int page, final boolean more) {
	// TODO Auto-generated method stub
		if (more) {
			mPager.curpage++;
		}
		mainpostDatetask = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return HttpJsonTool.getInstance().getMyTingshuoList_Commit(
						getActivity(), maxId, minId, page,-1,-1);
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				boolean hasmore = false;
				if (result.startsWith(HttpJsonTool.ERROR403)) {
					gotoLoginView();
				} else if (result.startsWith(HttpJsonTool.ERROR)) {
					//T.showLong(getActivity(),
							//result.replace(HttpJsonTool.ERROR, ""));
				} else if (result.startsWith(HttpJsonTool.SUCCESS)) {
					hasmore = refreshData(more);
				}
				if (more) {
					adapter.notifyDataSetChanged();
					mainpostListView.onRefreshComplete();
				} else {
					mainpostListView.onRefreshComplete();
				}
				mainpostListView.setMode(hasmore ? Mode.BOTH
						: Mode.PULL_FROM_START);
				adapter.notifyDataSetChanged();
			}
		};
		mainpostDatetask.execute();
	}
	@Override
	protected boolean refreshData(boolean more) {
		if (!more) {
			listData.clear();
		}
		MyCommitedMainPostListHelper helper = new MyCommitedMainPostListHelper(
				getActivity());
		ArrayList<mainPostListHolder> holders;
		if (more) {
			L.i(mPager.curpage * mPager.pagesize + "");
			holders = helper.selectData(mPager.curpage * mPager.pagesize,
					mPager.pagesize,-1,-1);
		} else {
			holders = helper.selectData(0, mPager.pagesize,-1,-1);
		}
		helper.close();
		for (mainPostListHolder holder : holders) {
			L.i("id:" + holder.getId() + "");
			insertHolderData(holder);
		}
		return holders.size() == mPager.pagesize;

	}
}
