/* SotaRepeat.java
 * 音声認識結果をTTSでオウム返しするアプリ
 */

package com.example.sotarepeat;

import jp.vstone.RobotLib.CPlayWave;
import jp.vstone.RobotLib.CRobotMem;
import jp.vstone.RobotLib.CSotaMotion;
import jp.vstone.sotatalk.MotionAsSotaWish;
import jp.vstone.sotatalk.SpeechRecog;
import jp.vstone.sotatalk.SpeechRecog.RecogResult;
import jp.vstone.sotatalk.TextToSpeechSota;

public class SotaRepeat {
	static final String TAG = "SotaRepeat";
	static final int LOOP_NUM = 10;

	public static void main(String[] args) {
		//VSMDと通信ソケット・メモリアクセス用クラス
		CRobotMem mem = new CRobotMem();
		//Sota用モーション制御クラス
		CSotaMotion motion = new CSotaMotion(mem);
		SpeechRecog recog = new SpeechRecog(motion);
		MotionAsSotaWish wish = new MotionAsSotaWish(motion);

		if (mem.Connect()) {
			//Sota仕様にVSMDを初期化
			motion.InitRobot_Sota();
			motion.ServoOn();

			for (int i=0; i<LOOP_NUM; i++) {
				// 音声認識（10秒までの音声に対応）
				RecogResult result = recog.getRecognition(10000);
				if (result.recognized) {
					// 認識結果をTextToSpeech
					wish.Say(result.getBasicResult(), MotionAsSotaWish.MOTION_TYPE_TALK);
					//CPlayWave.PlayWave(TextToSpeechSota.getTTSFile(result.getBasicResult()),true);

					if(result.getBasicResult().contains("おわり")){
						CPlayWave.PlayWave(TextToSpeechSota.getTTSFile("終了するよ"),true);
						break;
					}
				}

				if (i == LOOP_NUM - 1) {
					CPlayWave.PlayWave(TextToSpeechSota.getTTSFile("疲れたので終了するよ"), true);
				}

			}

			motion.ServoOff();

		}

	}

}
