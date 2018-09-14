#!/bin/sh

# disable filename globbing
set -f

echo "Content-Type: text/html"
echo

echo "<HTML><HEAD>"
echo "<TITLE>Visites del CGI</TITLE>"

echo v= $(cat comptador.txt)
echo v= expr $v + 1
echo $v > comptador.txt

echo "</HEAD>"
echo "<BODY>"
echo "<H1>En portem $v "
echo "</H1>"
echo "</H1>"
echo "</BODY>"
echo "</HTML>"
