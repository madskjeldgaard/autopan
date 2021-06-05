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

PanFunc{
	classvar <numChansOut;

	*ar{|sig, numChannelsIn=1, numChannelsOut=2|
		numChansOut = numChannelsOut;
		^this.embedWithPanner(numChannelsIn, sig)
	}

	*embedWithPanner{|numChannelsIn=1, sig|
		^SynthDef.wrap(
			this.getPanFunc(numChannelsIn: numChannelsIn, numChannelsOut: numChansOut),  
			prependArgs: [sig]
		)
	}

	// Deduce a panning function from number of channels in and number of channels out
	// Return a function to be used with SynthDef.wrap
	*getPanFunc{|numChannelsIn=1, numChannelsOut=2|
		var panFunc = case
		// Mono output
		{ numChannelsOut == 1 } { 
			if(numChannelsIn > 1, { 
				{|sig|sig.sum}		
			}, {
				{|sig|sig}
			}) 
		}
		// Stereo output
		{ numChannelsOut == 2 } { 
			case 
			// Mono input
			{ numChannelsIn == 1 } { 
				{|sig, pan=0, panFreq=1, autopan=0, panShape=1| 
					var panner = AutoPan.ar(pan:pan, panFreq:panFreq, autopan:autopan, panShape:panShape);
					Pan2.ar(sig, panner)
				} 
			}
			// Stereo input
			{ numChannelsIn == 2 } { 
				{|sig, pan=0, panFreq=1, autopan=0, panShape=1| 
					var panner = AutoPan.ar(pan:pan, panFreq:panFreq, autopan:autopan, panShape:panShape);
					Balance2.ar(sig[0], sig[1], panner) 
				}		
			}
		}
		// Multichannel output
		{numChannelsOut > 2} { 
			case
			// Mono input
			{ numChannelsIn == 1 } { 
				{|sig, pan=0, width=2, orientation=0.5, panFreq=1, autopan=0, panShape=1|
					var panner = AutoPan.ar(pan:pan, panFreq:panFreq, autopan:autopan, panShape:panShape);

					PanAz.ar(
						numChannelsOut, 
						sig, 
						panner, 
						width: width, 
						orientation: orientation
					) 
				}
			}
			// Stereo input
			{ numChannelsIn > 1 } { 
				{|sig, pan=0, spread=1, width=2.0, orientation=0.5, levelComp=true, panFreq=1, autopan=0, panShape=1|
					var panner = AutoPan.ar(pan:pan, panFreq:panFreq, autopan:autopan, panShape:panShape);

					SplayAz.ar(
						numChannelsOut, 
						sig,  
						spread: spread,  
						level: 1,  
						width: width,  
						center: panner,  
						orientation: orientation,  
						levelComp: levelComp
					)
				}
			};

		};

		^panFunc
	}
}

	
