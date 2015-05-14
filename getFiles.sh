#!/bin/bash
wget -nc http://cs108.epfl.ch/files/lausanne.osm.gz
wget -nc http://cs108.epfl.ch/files/berne.osm.gz
wget -nc http://cs108.epfl.ch/files/interlaken.osm.gz
wget -nc http://cs108.epfl.ch/files/imhof-dems.zip
unzip imhof-dems.zip
rm imhof-dems.zip
