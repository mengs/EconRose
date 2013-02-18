import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import controlP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Economy_Rosalization_All extends PApplet {


/* 
 Data Vizualization Project
 Economy Rosalization @
 
 Meng Shi Feb 16, 2013
 lolaee@gmail.com
 Master Student @ Tangible Interaction Design
 
 Project2 for course Interactive Art and Computational Design
 by Prof Golan Levin @ CMU School of Art
 */


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

//control variables
String query = "";

// Show all or not to show
boolean  showAll = false;
boolean  sparkling = false;
boolean  one = false;
boolean  scroll = false;
boolean Golbal_Economy_Rosalization=false;

// About Info variables
int myColor;
int c1, c2;
float n, n1;


public void setup() { 




  cp5 = new ControlP5(this);

  // change the default font to arial
  PFont p = createFont("Trebuchet", 9);
  cp5.setControlFont(p);

  cp5.addTextfield("query")
    .setPosition(20, 50)
      .setSize(180, 30)
        .setFont(p)
          .setFocus(true)
            //            .setColor(0xbbff0000)
            .setColorBackground(0xffffffff)
              .setColorForeground(0xffccffff)
                .setColorActive(0xffccffff)
                  .setColorValue(0x0000000)

                    ;


  cp5.addBang("next")
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

  cp5.addToggle("sparkling")
    .setPosition(340, 50)
      .setSize(60, 30)
        .setColorForeground(0x66ff1100)
          .setColorBackground(0xaaff1100)
            .setColorActive(0xaaff8800)
              .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
                ; 
  cp5.addToggle("one")
    .setPosition(410, 50)
      .setSize(50, 30)
        .setColorForeground(0xaaccee00)
          .setColorBackground(0xaacccc00)
            .setColorActive(0xaaffee00)
              .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
                ; 
  cp5.addToggle("scroll")
    .setPosition(470, 50)
      .setSize(50, 30)
        .setColorForeground(0x66112200)
          .setColorBackground(0x66992200)
            .setColorActive(0x66dd2200)
              .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
                ; 
  cp5.addToggle("Golbal_Economy_Rosalization")
    .setPosition(1280-250, 50)
      .setSize(200, 30)
        .setColorForeground(0xff11ff00)
          .setColorBackground(0xff44bb00)
            .setColorActive(0xffbbff00)
              .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
                ; 


  textFont(p);

  size(1280, 720);
  //  size(9400, 900);
  background(255);
  smooth();


  //arbituary number not related to data but determine the angle spiral turned
  posx=0;
  posy=0;
  for (int i=0;i<15000;i+=1) {
    incre+=random(0.0f, 0.05f);
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
    float c= map(data.getFloat(i, 2), data.getColumnMin(2), data.getColumnMax(2), 0.0f, 0.1f); // population
    if (c>0.05f) {
      c=0.05f;
    }
    float d= map(data.getFloat(i, 4), data.getColumnMin(4), data.getColumnMax(4), 0.0f, 0.05f);//CurrentAccountBalance
    if (d>0.25f) {
      d=0.25f;
    }
    float e= map(data.getFloat(i, 0), data.getColumnMin(0), data.getColumnMax(0), 100, 10000); // GDP in US dollars
    if (e>7500) {
      d=7500;
    }
    countriesRoses[i]=new RoseLine(posx, posy, a, b, c, d, e, angNoiseList);
  }
}

//begin generate flower for each country
public void draw() {


  background(255);

  if (sparkling==true) {

    showAll=false;
    one=false;
    scroll=false;
    Golbal_Economy_Rosalization=false;

    posx=width/2-100;
    posy=height/2-100; 

    for (int i=1;i<186;i++) {

      posx=width*2%(i+1)+noise(width/3, width/2)*random(255*i);
      posy=height*2%(i+1)+noise(height/3, height/2)*random(255*i); 
      posx=map(posx, 0, 255*i/1.52f, 0, width*2);
      posy=map(posy, 0, 255*i/1.52f, 0, height*2);
      stroke(sqrt(i*3)*i, abs(255-i*2.1f), sqrt(300)*i/255);
      countriesRoses[i].drawRose(posx, posy);
    }
  }

  if (showAll==true) {
    sparkling=false;
    one=false;
    scroll=false;
    Golbal_Economy_Rosalization=false;

    for (int i=0;i<186;i++) {
      posx= 200+ PApplet.parseInt((i%62)) * (2*9400)/62; // width-200=2*9400
      posy= 200+ PApplet.parseInt((i/62))* (900)/3; // height-300=900

      //      posx=map(posx,0,200+2*9400,0,width);
      //      posx=map(posy,0,1200,0,height/10);

      pushMatrix();
      scale(0.6f);
      stroke(sqrt(i*3)*i, abs(255-i*2.1f), sqrt(300)*i/255);
      countriesRoses[i].drawRose(posx/10, posy);
      popMatrix();
    }
  } 
  if ( one==true) {
    sparkling=false;
    showAll=false;
    scroll=false;
    Golbal_Economy_Rosalization=false;
    for (int i=0;i<186;i++) {
      posx= width/2-100;
      posy= height/2-100;
      stroke(sqrt(i*3)*i, abs(255-i*2.1f), sqrt(300)*i/255, random(30, i));
      countriesRoses[i].drawRose(posx, posy);
    }
  }
  if ( scroll==true) {
    sparkling=false;
    showAll=false;
    one=false;
    Golbal_Economy_Rosalization=false;
    posx= width/2-100;
    posy= height/2-100;
    float mx=constrain(mouseX, 35, width-35);
    float pointer = map(mx, 35, width-35, 0, 185);

    fill(sqrt(pointer*3)*pointer, abs(255-pointer*2.1f), sqrt(300)*pointer/255);
    ellipse(35, posy+100, 5, 5);
    ellipse(width-35, posy+100, 5, 5);
    ellipse(mx, posy+100, 5, 5);
    textAlign(CENTER, CENTER);
    textSize(13);
    text("A", 20, posy+100);
    text("Z", width-20, posy+100);
    textSize(28);
    text(countries[PApplet.parseInt(pointer)], width/2, posy+300);
    stroke(sqrt(pointer*3)*pointer, abs(255-pointer*2.1f), sqrt(300)*pointer/255);

    line(35, posy+100, width-35, posy+100);
    countriesRoses[PApplet.parseInt(pointer)].drawRose(posx, posy);
  }
  if (Golbal_Economy_Rosalization==true) {
    fill(myColor);
    myColor = lerpColor(c1, c2, n);
    n += (1-n)* 0.05f;
    //    noStroke();
    pushMatrix();
    translate(width-250, 50);
    int lineheight=15;
    String[] aboutString= {"Golbal Economy Rosalization", "", "Visualizting Golbal Economy","in Terms of Rose-like Line Sketch","",
        "Data Source", "  October 2012 World Economic Outlook ", "  IMF http://www.imf.org/", "", 
      "Data Units:", "  GDP", "  Inflation", "  Population", "  Government Revenue ", "","Total sample","  185 Countries","","A Data Generative Art Project", "Spring 2013 IACD@CMU","Source Code","https://github.com/mengs/EconRose"};
        for (int i=0;i<aboutString.length;i++) {
        text(aboutString[i], 10, 50+lineheight*i);
      }
      popMatrix();
    }


    int queryIndex;
    query=cp5.get(Textfield.class, "query").getText();

    for (int i=0;i<countries.length;i++) {
      if (countries[i].equals(query)) {
        showAll=false;
        scroll=false;
        sparkling=false;
        one=false;
        Golbal_Economy_Rosalization=false;

        queryIndex=i;
        //      print(queryIndex);
        posx=width/2-100;
        posy=height/2-100;

        stroke(sqrt(i*3)*i, abs(255-i*2.1f), sqrt(300)*i/255);
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
  public void mousePressed() {
    next();
    redraw();
    query=cp5.get(Textfield.class, "query").getText();
  }

  public void next() {
    cp5.get(Textfield.class, "query").clear();
  }
  public void controlEvent(ControlEvent theEvent) {
    n = 0;
    if (Golbal_Economy_Rosalization==true) {

      showAll=false;
      scroll=false;
      sparkling=false;
      one=false;

      c1 = c2;
      c2 = color(60);
    }
    else {
      c1 = c2;
      c2 = color(0, 160, 100);
    }
  }

// first line of the file should be the column headers
// first column should be the row titles
// all other values are expected to be floats
// getFloat(0, 0) returns the first data value in the upper lefthand corner
// files should be saved as "text, tab-delimited"
// empty rows are ignored
// extra whitespace is ignored


/*
 a=data.getRowCount();  //without description line
 b=data.getColumnCount(); // without country
 d=data.getFloat(3,3); //without country and without descriptionline
 country,gdp,inflation,population,govRevenue,curAccBalance
 
 */
class FloatTable {
  int rowCount;
  int columnCount;
  float[][] data;
  String[] rowNames;
  String[] columnNames;


  FloatTable(String filename) {
    String[] rows = loadStrings(filename);

    String[] columns = split(rows[0], TAB);
    columnNames = subset(columns, 1); // upper-left corner ignored
    scrubQuotes(columnNames);
    columnCount = columnNames.length;

    rowNames = new String[rows.length-1];
    data = new float[rows.length-1][];

    // start reading at row 1, because the first row was only the column headers
    for (int i = 1; i < rows.length; i++) {
      if (trim(rows[i]).length() == 0) {
        continue; // skip empty rows
      }
      if (rows[i].startsWith("#")) {
        continue;  // skip comment lines
      }

      // split the row on the tabs
      String[] pieces = split(rows[i], TAB);
      scrubQuotes(pieces);

      // copy row title
      rowNames[rowCount] = pieces[0];
      // copy data into the table starting at pieces[1]
      data[rowCount] = parseFloat(subset(pieces, 1));

      // increment the number of valid rows found so far
      rowCount++;
    }
    // resize the 'data' array as necessary
    data = (float[][]) subset(data, 0, rowCount);
  }


  public void scrubQuotes(String[] array) {
    for (int i = 0; i < array.length; i++) {
      if (array[i].length() > 2) {
        // remove quotes at start and end, if present
        if (array[i].startsWith("\"") && array[i].endsWith("\"")) {
          array[i] = array[i].substring(1, array[i].length() - 1);
        }
      }
      // make double quotes into single quotes
      array[i] = array[i].replaceAll("\"\"", "\"");
    }
  }


  public int getRowCount() {
    return rowCount;
  }


  public String getRowName(int rowIndex) {
    return rowNames[rowIndex];
  }


  public String[] getRowNames() {
    return rowNames;
  }


  // Find a row by its name, returns -1 if no row found. 
  // This will return the index of the first row with this name.
  // A more efficient version of this function would put row names
  // into a Hashtable (or HashMap) that would map to an integer for the row.
  public int getRowIndex(String name) {
    for (int i = 0; i < rowCount; i++) {
      if (rowNames[i].equals(name)) {
        return i;
      }
    }
    //println("No row named '" + name + "' was found");
    return -1;
  }


  // technically, this only returns the number of columns 
  // in the very first row (which will be most accurate)
  public int getColumnCount() {
    return columnCount;
  }


  public String getColumnName(int colIndex) {
    return columnNames[colIndex];
  }


  public String[] getColumnNames() {
    return columnNames;
  }


  public float getFloat(int rowIndex, int col) {
    // Remove the 'training wheels' section for greater efficiency
    // It's included here to provide more useful error messages

    // begin training wheels
    if ((rowIndex < 0) || (rowIndex >= data.length)) {
      throw new RuntimeException("There is no row " + rowIndex);
    }
    if ((col < 0) || (col >= data[rowIndex].length)) {
      throw new RuntimeException("Row " + rowIndex + " does not have a column " + col);
    }
    // end training wheels

    return data[rowIndex][col];
  }


  public boolean isValid(int row, int col) {
    if (row < 0) return false;
    if (row >= rowCount) return false;
    //if (col >= columnCount) return false;
    if (col >= data[row].length) return false;
    if (col < 0) return false;
    return !Float.isNaN(data[row][col]);
  }


  public float getColumnMin(int col) {
    float m = Float.MAX_VALUE;
    for (int i = 0; i < rowCount; i++) {
      if (!Float.isNaN(data[i][col])) {
        if (data[i][col] < m) {
          m = data[i][col];
        }
      }
    }
    return m;
  }


  public float getColumnMax(int col) {
    float m = -Float.MAX_VALUE;
    for (int i = 0; i < rowCount; i++) {
      if (isValid(i, col)) {
        if (data[i][col] > m) {
          m = data[i][col];
        }
      }
    }
    return m;
  }


  public float getRowMin(int row) {
    float m = Float.MAX_VALUE;
    for (int i = 0; i < columnCount; i++) {
      if (isValid(row, i)) {
        if (data[row][i] < m) {
          m = data[row][i];
        }
      }
    }
    return m;
  } 


  public float getRowMax(int row) {
    float m = -Float.MAX_VALUE;
    for (int i = 1; i < columnCount; i++) {
      if (!Float.isNaN(data[row][i])) {
        if (data[row][i] > m) {
          m = data[row][i];
        }
      }
    }
    return m;
  }


  public float getTableMin() {
    float m = Float.MAX_VALUE;
    for (int i = 0; i < rowCount; i++) {
      for (int j = 0; j < columnCount; j++) {
        if (isValid(i, j)) {
          if (data[i][j] < m) {
            m = data[i][j];
          }
        }
      }
    }
    return m;
  }


  public float getTableMax() {
    float m = -Float.MAX_VALUE;
    for (int i = 0; i < rowCount; i++) {
      for (int j = 0; j < columnCount; j++) {
        if (isValid(i, j)) {
          if (data[i][j] > m) {
            m = data[i][j];
          }
        }
      }
    }
    return m;
  }
}

//A line flower 2D drawing with several variables

class RoseLine {

  float x, y, lastx, lasty;
  float posx, posy;// define drawing prosition
  float thisRadius;
  float radius;
  float radiusNoise;
  float ampvarNoise;
  float ampvarRadius;
  float ampvarRadiusNoise;
  float ampvarAngLength;
  float[] angNoise;
  //  color fillcolor;



  /* This is a sample numbers helps translate the order of magnitude in the data
   float radiusNoise=random(10);
   float ampvarNoise=random(290);
   float ampvarRadius=random(0.0, 0.05);
   float ampvarRadiusNoise=random(0.25);
   float ampvarAngLength=random(5000, 15000);
   */

  RoseLine( float _posx, float _posy, float a, float b, float c, float d, float e, float[] f) {
    posx= _posx;
    posy= _posy;
    radiusNoise=a;
    ampvarNoise=b;
    ampvarRadius=c;
    ampvarRadiusNoise=d;
    ampvarAngLength=e;
    angNoise=f;
    //    fillcolor=fc;
  }

  public void drawRose( float posx, float posy) {
   
    radius =10;
    lastx=-999;
    lasty=-999;

    for (int ang=0;ang<ampvarAngLength;ang+=5) {
      radius+=ampvarRadius;
      radiusNoise+=ampvarRadiusNoise;
      thisRadius=radius+angNoise[ang]*ampvarNoise-100;
      float rad=radians(ang);
      pushMatrix();
      translate(posx, posy);
      x=100+(thisRadius*cos(rad));
      y=100+(thisRadius*sin(rad));
      if (lastx>-999) {
        line(x, y, lastx, lasty);
      }
      lastx=x;
      lasty=y;
      popMatrix();
    }
  }
}

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--full-screen", "--bgcolor=#666666", "--stop-color=#cccccc", "Economy_Rosalization_All" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
