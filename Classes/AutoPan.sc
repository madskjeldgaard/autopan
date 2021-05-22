AutoPan {
	*ar{|pan=1, panFreq=1, autopan=0, panShape=(-1.0)|
		var panner = XFade2.ar(
			SinOsc.ar(panFreq), 
			LFSaw.ar(panFreq), 
			panShape
		);

		var crossfade = autopan.linlin(0.0,1.0,-1.0,1.0);

		// Scale panner
		panner = panner.linlin(-1.0,1.0,pan,(-1)*pan);
		^XFade2.ar(K2A.ar(pan), panner, crossfade);
	}

	*kr{|pan=1, panFreq=1, autopan=0, panShape=(-1.0)|
		var panner = XFade2.kr(
			SinOsc.kr(panFreq), 
			LFSaw.kr(panFreq), 
			panShape
		);

		var crossfade = autopan.linlin(0.0,1.0,-1.0,1.0);

		// Scale panner
		panner = panner.linlin(-1.0,1.0,pan,(-1)*pan);
		^XFade2.kr(pan, panner, crossfade);
	}

}


