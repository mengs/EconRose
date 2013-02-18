
/* 
 Data Vizualization Project
 Economy Rosalization @
 
 Meng Shi Feb 16, 2013
 lolaee@gmail.com
 
 Project2 for course Interactive Art and Computational Design
 by Prof Golan Levin @ CMU School of Art
 */

import processing.pdf.*;

FloatTable data;

RoseLine[] countriesRoses=new RoseLine[186];
String[] countries=new String[186];
float[] inflation=new float[186];
float[] govRevenue=new float[186];
float[] population=new float[186];
float[] curAccBalance=new float[186];
float[] gdp=new float[186];

//variables for generating a list of perlin noise
// different countries will share the same list of perlin noise
int incre=10;
float[] angNoiseList= new float[15000];

// data related variables
int rowCount;
int colCount;

// position related variables
int posx;
int posy;

void setup() { 

  //  size(19000, 1200, PDF, "file.pdf");
  size(1280, 720, PDF, "file.pdf");
  background(255);
  smooth();

  //arbituary number not related to data but determine the angle spiral turned
  posx=0;
  posy=0;
}

//begin generate flower for each country
void draw() {

  for (int i=0;i<15000;i+=1) {
    incre+=random(0.0, 0.05);
    angNoiseList[i]=noise(incre*sqrt(i));
  }

  data = new FloatTable("roseEcon.txt");

  for (int i=0; i<186; i++) {

    countries[i]=data.getRowName(i);
    float a= map(data.getFloat(i, 3), data.getColumnMin(3), data.getColumnMax(3), 0, 20); // Government Revenue
    float b= map(data.getFloat(i, 1), data.getColumnMin(1), data.getColumnMax(1), 0, 95000); //inflation
    if (b>300) { // let the nominally big number not influence scale
      b=300;
    }
    float c= map(data.getFloat(i, 2), data.getColumnMin(2), data.getColumnMax(2), 0.0, 0.1); // population
    if (c>0.05) {
      c=0.05;
    }
    float d= map(data.getFloat(i, 4), data.getColumnMin(4), data.getColumnMax(4), 0.0, 0.05);//CurrentAccountBalance
    if (d>0.25) {
      d=0.25;
    }
    float e= map(data.getFloat(i, 0), data.getColumnMin(0), data.getColumnMax(0), 100, 10000); // GDP in US dollars
    if (e>7500) {
      d=7500;
    }
    if (i==4) {
      println(a+","+b+","+ c+","+  d+","+ e);
    } 
//    if (keyPressed == true && keyCode == UP) {
//      posx= 100+ int((i%62)) * (width-200)/62; // width-200=2*9400
//      posy= 100+ int((i/62))* (height-300)/3; // height-300=900
//    }
//    else {
      posx=1280/2-100;
      posy=720/2-100;
//    }
    countriesRoses[i]=new RoseLine(posx, posy, a, b, c, d, e, angNoiseList);
  }

  for (int i=0; i<186; i++) {
    countriesRoses[i].drawRose();
    println(countries[i]);
  }
  //  countriesRoses[15].drawRose();
  //  println(countries[15]);

  //
  //  countriesRoses[177].drawRose();
  //  println(countries[177]);
  // Exit the program 
   println("Finished.");
   exit();
}

/* This is a sample numbers helps translate the order of magnitude in the data
 float radiusNoise=random(10); a 
 float ampvarNoise=random(290);b 
 float ampvarRadius=random(0.0, 0.05); c 
 float ampvarRadiusNoise=random(0.25); d 
 float ampvarAngLength=random(5000, 15000); e
 */
