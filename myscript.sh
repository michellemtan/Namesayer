#!/bin/bash

cd ~

ffmpeg -f alsa -i hw:0 -acodec libmp3lame -filter_complex showvolume=b=5:v=0:h=75:w=600[a] -map [a] -window_title "Microphone Volume" -f sdl - 2>/dev/null

