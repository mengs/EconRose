//A line flower 2D drawing with several variables

class RoseLine {

  float x, y, lastx, lasty;
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

  RoseLine( float a, float b, float c, float d, float e, float[] f) {
    radiusNoise=a;
    ampvarNoise=b;
    ampvarRadius=c;
    ampvarRadiusNoise=d;
    ampvarAngLength=e;
    angNoise=f;
    //    fillcolor=fc;
  }

  void drawRose() {
    radius =10;
    lastx=-999;
    lasty=-999;

    for (int ang=0;ang<ampvarAngLength;ang+=5) {
      radius+=ampvarRadius;
      radiusNoise+=ampvarRadiusNoise;
      thisRadius=radius+angNoise[ang]*ampvarNoise-100;
      float rad=radians(ang);
      x=width/2+(thisRadius*cos(rad));
      y=height/2+(thisRadius*sin(rad));
      if (lastx>-999) {
        line(x, y, lastx, lasty);
      }
      lastx=x;
      lasty=y;
    }
  }
}

