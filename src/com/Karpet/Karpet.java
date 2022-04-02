package com.Karpet;

import java.util.Arrays;
import java.util.Random;
import java.util.Stack;
import java.util.ArrayList;

public class Karpet {
    private boolean[][] field;// поле
    private ArrayList<ArrayList<Vector2>> clusters;
    private Stack<Vector2> stack;
    private ArrayList<Vector2> points;


    private Karpet(int fieldSize) {
        this.field = new boolean[fieldSize][fieldSize];
        for(int i = 0; i < fieldSize; i++) Arrays.fill(this.field[i], false);
        this.clusters = new ArrayList<>();
        this.points = new ArrayList<>();
        this.stack = new Stack<>();
        //for(int i = 0; i < fieldSize; i++) Arrays.fill(this.clusters[i], 0);
    }

    private void paintCertainArea(Vector2 pnt1, Vector2 pnt2) {
        for(int i = pnt1.y; i < pnt2.y; i++) {
            for(int j = pnt1.x; j < pnt2.x; j++) {
                this.field[i][j] = true;
            }
        }
    }

    private void paint(int iteration, Vector2 pnt1, Vector2 pnt2, int size) {
        // pnt1, ... это точки углов квадрата
        if(iteration == 0) return;
        int step = size / 3;
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                var newpnt1 = pnt1.add(new Vector2(j * step, i * step));
                var newpnt2 = pnt2.add(new Vector2((j - 2) * step, (i - 2) * step));

                if(i == 1 && j == 1) {
                    this.paintCertainArea(
                            newpnt1,
                            newpnt2
                    );
                    continue;
                }

                this.paint(
                        iteration - 1,
                        newpnt1,
                        newpnt2,
                        step
                        );
            }
        }
    }

    private void initialization() {
        for(int i = 0; i < this.field.length; i++) {
            for (int j = 0; j < this.field[i].length; j++) {
                if (this.field[i][j]) this.points.add(new Vector2(i, j));
            }
        }
    }

    private void dfs(Vector2 P, double dist, int currentClusterNumber) {
        this.stack.push(P);
        this.points.remove(P);

        this.clusters.get(currentClusterNumber).add(P);
        Vector2 P1 = this.near(P, dist);
        //if(P1 != Vector2.nullVector) {
        if(P1.x != 0 || P1.y != 0) {
            //this.clusters[P1.y][P1.x] = currentClusterNumber;
            this.dfs(P1, dist, currentClusterNumber);
        }
        else {
            this.stack.pop();
            if(this.stack.isEmpty()) return;
            this.dfs(this.stack.pop(), dist, currentClusterNumber);
        }
    }
    private void dfsCaller(double k) {
        var rnd = new Random(System.currentTimeMillis());
        int currentClusterNumber = 0;
        while(!this.points.isEmpty()) {
            var randIndex = rnd.nextInt(this.points.size());
            var rndPoint = this.points.get(randIndex);
            this.clusters.add(currentClusterNumber, new ArrayList<>());
            this.dfs(rndPoint, k, currentClusterNumber);
            k *= updateD(currentClusterNumber);
            currentClusterNumber++;
        }
    }

    private double updateD(int clasterNumber) {
        var sumDist = 0.0d;
        for(var point : this.clusters.get(clasterNumber)) {
            for(var point2 : this.clusters.get(clasterNumber)) {
                sumDist += distance(point, point2);
            }
        }
        return sumDist / (fact(this.clusters.get(clasterNumber).size() - 1) * 2);
    }
    private static int fact(int n) {
        if(n <= 1) return 1;
        return fact(n - 1) * n;
    }

    private static double distance(Vector2 a, Vector2 b) {
        return Math.sqrt(Math.pow((a.x - b.x), 2) + Math.pow((a.y - b.y), 2));
    }

    private Vector2 near(Vector2 a, double average) {
        double maxdistance = distance(new Vector2(), new Vector2(81, 81));
        Vector2 vector = new Vector2();

        for(var point : this.points) {
            if ((distance(a, point) < maxdistance) && (distance(a, point) != 0)) {
                if (distance(a, point) == 1.0) return point;
                vector.x = point.x;
                vector.y = point.y;
                maxdistance = distance(a, vector);
            }
        }
        if (maxdistance > average) return new Vector2();
        return vector;
    }


    public static void main(String[] args) {
        int size = 27;
        int iteration = 1; // iteration = log_3(size)
        var kvr = new Karpet(size);
        kvr.paint(
                iteration,
                new Vector2(0, 0),
                new Vector2(size, size),
                size
        );
        kvr.initialization();
        for(int i = 0; i < kvr.field.length; i++) {
            for (int j = 0; j < kvr.field[i].length; j++)
                System.out.print((kvr.field[i][j] ? 1 : 0) + " ");
            System.out.print("\n");
        }
        kvr.dfsCaller(1);
        System.out.print(kvr.clusters);
    }
}
