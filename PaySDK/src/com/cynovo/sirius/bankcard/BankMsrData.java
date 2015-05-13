package com.cynovo.sirius.bankcard;

import android.util.Log;

import com.cynovo.sirius.util.MyLog;
import com.kivvi.jni.MsrInterface;

public class BankMsrData {

	private TrackData trackData = null;

	public BankMsrData() {

	}

	public boolean QueryMsrSwipe() {
		MyLog.d("PosService", "msr polling....");
		// modified by wanhaiping----------------------------------->begin
		// int retval = MsrInterface.poll(500);
		// int retval = MsrInterface.getEvent();
		int retval = MsrInterface.poll();
		// modified by wanhaiping-----------------------------------<end

		MyLog.i("debug", "MsrInterface.poll(-1); retval = "+String.valueOf(retval));

		// modified by wanhaiping----------------------------------->begin
		// if (retval == 0)
		// return true;
		// return false;
		if (retval == 0) {
			// MsrInterface.setEvent(-1);
			MyLog.d("PosService", "msr polling return");
			return true;
		} else {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
		// modified by wanhaiping-----------------------------------<end

	}

	public TrackData readTrackData() {
		MyLog.e("debug", "TrackData  readTrackData point1");
		trackData = new TrackData();
		MyLog.e("debug", "TrackData  readTrackData point2");
		trackData.setTrack(readTrack(0), readTrack(1), readTrack(2));
		MyLog.e("debug", "TrackData  readTrackData point3");
		return trackData;
	}

	public void clearData() {
		trackData = null;
	}

	public TrackData getTrackData() {
		return trackData;
	}

	private String readTrack(int trackNo) {
		MyLog.e("debug", "readTrack  point1");
		int length = MsrInterface.getTrackDataLength(trackNo);
		MyLog.e("debug", "readTrack  point2");
		MyLog.i("debug", "track " + String.valueOf(trackNo + 1) + " ===>>> length : " + String.valueOf(length));
		byte track[] = new byte[255];
		if (length > 0 && checkErrno(trackNo)
				&& MsrInterface.getTrackData(trackNo, track, 255) >= 0) {
			MyLog.e("debug", "readTrack  point3");
			String str = new String(track, 0, length);
			MyLog.i("read track " + String.valueOf(trackNo + 1), str);
			// added by wanhaiping---------------------------------------->begin
			// MsrInterface.setEvent(-1);
			// added by wanhaiping----------------------------------------<end
			return str;
		} else {
			MyLog.e("debug", "readTrack  point4");
			MyLog.i("read track " + String.valueOf(trackNo + 1), "error");

			// added by wanhaiping---------------------------------------->begin
			// MsrInterface.setEvent(-1);
			// added by wanhaiping----------------------------------------<end
			return null;
		}
	}

	private boolean checkErrno(int trackNo) {
		// modified by wanhaiping---------------------->begin
		// errno = MsrInterface.getTrackError(trackNo);
		// 老版本库
		// return errno >= 0;
		return true;
		// modified by wanhaiping-------------------------<end

		// 新版本库
		// if(trackNo == 0)
		// {
		// return ((errno & 0x09) == 0x09);
		// }
		// else if(trackNo == 1)
		// {
		// return ((errno & 0xd2) == 0xd2);
		// }
		// else if(trackNo == 2)
		// {
		// return ((errno & 0x24) == 0x24);
		// }
		// else
		// {
		// return false;
		// }
	}

	public class TrackData {
		private String track1 = null;
		private String track2 = null;
		private String track3 = null;

		public String getTrack1() {
			return track1;
		}

		public void setTrack1(String track1) {
			this.track1 = track1;
		}

		public String getTrack2() {
			return track2;
		}

		public void setTrack2(String track2) {
			this.track2 = track2;
		}

		public String getTrack3() {
			return track3;
		}

		public void setTrack3(String track3) {
			this.track3 = track3;
		}

		public void setTrack(String track1, String track2, String track3) {
			MyLog.d("PosService", "setTrack begin...");
			MyLog.d("PosService", "-------Track1  " + track1);
			MyLog.d("PosService", "-------Track2  " + track2);
			MyLog.d("PosService", "-------Track3  " + track3);
			MyLog.d("PosService", "setTrack end...");
			this.track1 = track1;// !=null?track1.replace("%", "").replace("?",
									// ""):null;
			this.track2 = track2;// !!=null?track2.replace(";", "").replace("?",
									// ""):null;
			this.track3 = track3;// !!=null?track3.replace(";", "").replace("?",
									// ""):null;
		}

		public boolean checkHaveEmv() {
			if (track2 != null) {
				String[] trackStrArray = track2.split("=");
				if (trackStrArray.length > 1 && trackStrArray[1].length() > 4) {
					char servercode = trackStrArray[1].charAt(4);
					if (servercode == '2' || servercode == '6')
						return true;
				}
			}
			return false;
		}

		public void clearData() {
			this.track1 = null;
			this.track2 = null;
			this.track3 = null;
		}

		public boolean isValid() {
			MyLog.d("PosService", "TO check valid begin...");
			MyLog.d("PosService", "-------Track1  " + track1);
			MyLog.d("PosService", "-------Track2  " + track2);
			MyLog.d("PosService", "-------Track3  " + track3);
			MyLog.d("PosService", "TO check valid end...");
			if (track2 != null && !track2.isEmpty()) {
				//return !isSmartCard(track2);
				return true;
			} else
				return false;
		}
		
		/**
		 * 判断卡片是否是接触式IC卡
		 * @return
		 */
		private boolean isSmartCard(String track) {
			if (track != null) {
				String rcard = track.split("=")[1];
				char[] temp = rcard.toCharArray();
				int length = temp.length;
				if (length < 5) {
					return false;
				} else {
					if (temp[4] == '1') {
						Log.i("debug", "MSR Card...");
						return false;
					} else if (temp[4] == '2' || temp[4] == '6') {
						//T=0		2
						//T=1		6
						Log.i("debug", "EMV Card...");
						return true;
					}
				}
			} else {
				return false;
			}
			return false;
		}


		public String getCardNumber() {
			if (track2 != null)
				return track2.split("=")[0];
			else
				return null;
		}

		public String getOwnerName() {
			if (track1 != null) {
				String[] trackStrArray = track1.split("\\^");
				if (trackStrArray.length > 1)
					return trackStrArray[1];
				else
					return null;
			} else
				return null;
		}

		public String toString() {
			return "TrackData [track1=" + track1 + ", track2=" + track2
					+ ", track3=" + track3 + "]";
		}
	}
}
