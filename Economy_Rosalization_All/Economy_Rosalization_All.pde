
/* 
 Data Vizualization Project
 Economy Rosalization @
 
 Meng Shi Feb 16, 2013
 lolaee@gmail.com
 
 Project2 for course Interactive Art and Computational Design
 by Prof Golan Levin @ CMU School of Art
 */

import controlP5.*;
ControlP5 cp5;
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
float posx;
float posy;

//query variables
String query = "";

// Show all or not to show
boolean  showAll = false;

void setup() { 

  PFont font = createFont("arial", 15);

  cp5 = new ControlP5(this);

  cp5.addTextfield("query")
    .setPosition(20, 50)
      .setSize(180, 30)
        .setFont(font)
          .setFocus(true)
            .setColor(color(255))
              ;


  cp5.addBang("clear")
    .setPosition(210, 50)
      .setSize(50, 30)
        .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
          ; 

  cp5.addToggle("showAll")
    .setPosition(270, 50)
      .setSize(60, 30)
        .setColorForeground(0xffaa0000)
          .setColorBackground(0xff660000)
            //            .setColorLabel(0xffdddddd)
            .setColorValue(0xffff88ff)
              .setColorActive(0xffff0000)
                .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
                  ;


  textFont(font);

  size(1280, 720);
  //  size(9400, 900);
  background(255);
  smooth();


  //arbituary number not related to data but determine the angle spiral turned
  posx=0;
  posy=0;
  for (int i=0;i<15000;i+=1) {
    incre+=random(0.0, 0.05);
    angNoiseList[i]=noise(incre*sqrt(i));
  }

  data = new FloatTable("roseEcon.txt");

  for (int i=0; i<186; i++) {

    countries[i]=data.getRowName(i);
  }
  for (int i=0; i<186; i++) {

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
    countriesRoses[i]=new RoseLine(posx, posy, a, b, c, d, e, angNoiseList);
  }
}

//begin generate flower for each country
void draw() {


  background(255);

  if (showAll==true) {
    for (int i=0;i<186;i++) {
      posx= 200+ int((i%62)) * (2*9400)/62; // width-200=2*9400
      posy= 200+ int((i/62))* (900)/3; // height-300=900

      //      posx=map(posx,0,200+2*9400,0,width);
      //      posx=map(posy,0,1200,0,height/10);

      pushMatrix();
      scale(0.6);
      countriesRoses[i].drawRose(posx/10, posy);
      popMatrix();
    }
  } 




  int queryIndex;
  query=cp5.get(Textfield.class, "query").getText();
  
  for (int i=0;i<countries.length;i++) {
    if (countries[i].equals(query)) {
      showAll=false;
      queryIndex=i;
//      print(queryIndex);
      posx=width/2-100;
      posy=height/2-100;
      countriesRoses[queryIndex].drawRose(posx, posy);

    }
  }
}
/* This is a sample numbers helps translate the order of magnitude in the data
 float radiusNoise=random(10); a 
 float ampvarNoise=random(290);b 
 float ampvarRadius=random(0.0, 0.05); c 
 float ampvarRadiusNoise=random(0.25); d 
 float ampvarAngLength=random(5000, 15000); e
 */
void mousePressed() {
  clear();
  redraw();
  query=cp5.get(Textfield.class, "query").getText();
}

public void clear() {
  cp5.get(Textfield.class, "query").clear();
}
