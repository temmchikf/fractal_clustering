package com.Karpet;

public class Vector2 { // класс для двумерных целочисленных векторов
    int x;
    int y;
    public static final Vector2 nullVector = new Vector2();

    public Vector2() { // конструктор, создающий нулевой вектор
        this.x = 0;
        this.y = 0;
    }
    public Vector2(int x, int y) { // конструктор по конкретным координатам
        this.x = x;
        this.y = y;
    }

    public Vector2 add(Vector2 o) { // сложение
        return new Vector2(this.x + o.x, this.y + o.y);
    }
    public int scalarProduct(Vector2 o) { // скалярное произведение
        return this.x * o.x + this.y * o.y;
    }

    @Override
    public String toString() {
        String ans = "";
        ans += x;
        ans += " ";
        ans += y;
        return ans;
    }
}
