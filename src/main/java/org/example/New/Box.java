package org.example.New;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class Box {
    Random r=new Random();

    int x,y;
    volatile boolean[] walls ={false,false,false,false} ;//left right top bottom
    boolean visited;
    ArrayList<Box> neighbors=new ArrayList<>();
    Box(int x_,int y_){
        x=x_;
        y=y_;
    }

    public void addNeighbors(Box[][] box){

        if(x>0){
            neighbors.add(box[this.x-1][this.y]);
        }
        if(y>0){
            neighbors.add(box[this.x][this.y-1]);
        }
        if(x<box.length-1){
            neighbors.add(box[this.x+1][this.y]);
        }
        if(y<box.length-1){
            neighbors.add(box[this.x][this.y+1]);
        }

    }
    public boolean hasUnvisited(){
        for (Box neighbor : neighbors) {
            if (!neighbor.visited) {
                return true;
            }
        }
       return  false;

    }
    protected  Box giveARandoOne() {

        int index=(r.nextInt(neighbors.size()));
         Box b = neighbors.get(index);

            while (b.visited ) {

                neighbors.remove(b);
                index=r.nextInt(neighbors.size());
                b = neighbors.get(index);

            }
            b.visited = true;
            neighbors.remove(b);

        return b;
    }

    public List<Box> getAllNeighbors(){
        return  neighbors;
    }
}
