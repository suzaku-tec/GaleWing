import { default as axios } from 'axios';
import { IElementEvent } from './elementEvent';

//axios=通信するやつ
//httpを省略するとうまく接続できなかったのでしっかり書いておく。
const rpc = axios.create({ baseURL: 'http://localhost:50021', proxy: false });

export default class PlaySound implements IElementEvent {
  private context = new AudioContext();
  private talkFunc: (() => void) | undefined = undefined;

  setTalking(func: () => void) {
    this.talkFunc = func;
  }

  execute() {
    if (this.talkFunc) {
      this.talkFunc();
    }
  }

  //text:喋ってもらいたい言葉
  async genAudio(text: string | null) {
    console.log('genAudio() text:', text);

    if (!text) {
      return Promise.reject();
    }

    /* まずtextを渡してsynthesis宛のパラメータを生成する、textはURLに付けるのでencodeURIで変換しておく。*/
    const audio_query = await rpc.post('audio_query?text=' + encodeURI(text) + '&speaker=1');

    //audio_queryで受け取った結果がaudio_query.dataに入っている。
    //このデータをメソッド:synthesisに渡すことで音声データを作ってもらえる
    //audio_query.dataはObjectで、synthesisに送る為にはstringで送る必要があるのでJSON.stringifyでstringに変換する
    const synthesis = await rpc.post('synthesis?speaker=1', JSON.stringify(audio_query.data), {
      responseType: 'arraybuffer',
      headers: {
        accept: 'audio/wav',
        'Content-Type': 'application/json',
      },
    });

    let arrayBuffer = synthesis.data;
    console.log(arrayBuffer);

    return new Promise<void>((resolve, reject) => {
      if (arrayBuffer instanceof ArrayBuffer) {
        // The 2nd argument for decodeAudioData
        let successCallback = (audioBuffer: AudioBuffer): void => {
          /* audioBuffer is the instance of AudioBuffer */
          let source = createAudioBufferSource(this.context, audioBuffer);
          source.onended = () => {
            resolve();
          };

          // Start audio
          source.start(0);
        };
        // The 3rd argument for decodeAudioData
        let errorCallback = (error: { message: any }) => {
          if (error instanceof Error) {
            window.alert(error.message);
          } else {
            window.alert('Error : "decodeAudioData" method.');
          }
          reject();
        };

        // Create the instance of AudioBuffer (Asynchronously)
        this.context.decodeAudioData(arrayBuffer, successCallback, errorCallback);
      }
    });
  }
}

function createAudioBufferSource(context: AudioContext, audioBuffer: AudioBuffer) {
  let source = context.createBufferSource();
  // Set the instance of AudioBuffer
  source.buffer = audioBuffer;
  // Set parameters
  source.loop = false;
  source.loopStart = 0;
  source.loopEnd = audioBuffer.duration;
  source.playbackRate.value = 1.0;

  // AudioBufferSourceNode (Input) -> AudioDestinationNode (Output)
  source.connect(context.destination);

  return source;
}
