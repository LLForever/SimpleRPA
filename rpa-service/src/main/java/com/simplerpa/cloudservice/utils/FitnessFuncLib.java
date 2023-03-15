package com.simplerpa.cloudservice.utils;

import java.util.Arrays;

public class FitnessFuncLib {
    public static double getFitFromFunc(double[] var, int type){
        if(type == 1){
            return Func1(var);
        }else if(type == 2){
            return Func2(var);
        }else if(type == 3){
            return Func3(var);
        }else if(type == 4){
            return Func4(var);
        }else if(type == 5){
            return Func5(var);
        }else if(type == 6){
            return Func6(var);
        }else if(type == 7){
            return Func7(var);
        }else if(type == 8){
            return Func8(var);
        }else if(type == 9){
            return Func9(var);
        }else if(type == 10){
            return Func10(var);
        }else if(type == 11){
            return Func11(var);
        }else if(type == 12){
            return Func12(var);
        }else if(type == 13){
            return Func13(var);
        }
        return Double.MAX_VALUE;
    }

    public static double[] getLow(int type){
        if(type == 1){
            return getFunc1Low();
        }else if(type == 2){
            return getFunc2Low();
        }else if(type == 3){
            return getFunc3Low();
        }else if(type == 4){
            return getFunc4Low();
        } else if(type == 5){
            return getFunc5Low();
        }else if(type == 6){
            return getFunc6Low();
        }else if(type == 7){
            return getFunc7Low();
        }else if(type == 8){
            return getFunc8Low();
        }else if(type == 9){
            return getFunc9Low();
        }else if(type == 10){
            return getFunc10Low();
        }else if(type == 11){
            return getFunc11Low();
        }else if(type == 12){
            return getFunc12Low();
        }else if(type == 13){
            return getFunc13Low();
        }
        return null;
    }

    public static double[] getUp(int type){
        if (type == 1){
            return getFunc1Up();
        }else if(type == 2){
            return getFunc2up();
        }else if(type == 3){
            return getFunc3Up();
        }else if(type == 4){
            return getFunc4Up();
        }else if(type == 5){
            return getFunc5Up();
        }else if(type == 6){
            return getFunc6Up();
        }else if(type == 7){
            return getFunc7Up();
        }else if(type == 8){
            return getFunc8Up();
        }else if(type == 9){
            return getFunc9Up();
        }else if(type == 10){
            return getFunc10Up();
        }else if(type == 11){
            return getFunc11Up();
        }else if(type == 12){
            return getFunc12Up();
        }else if(type == 13){
            return getFunc13Up();
        }
        return null;
    }

    public static double Func1(double[] var){
        double sum=0;
        for(int i=0;i<var.length;i++)
        {
            sum+=var[i]*var[i];
        }
        return sum;
    }

    public static double[] getFunc1Low(){
        int dimension = 30;
        double[] d = new double[dimension];
        for(int j=0; j<dimension; j++){
            d[j] = -100;
        }
        return d;
    }

    public static double[] getFunc1Up(){
        int dimension = 30;
        double[] d = new double[dimension];
        for(int j=0; j<dimension; j++){
            d[j] = 100;
        }
        return d;
    }

    public static double Func2(double[] var){
        double sum = 0;
        for(int i=0; i<var.length; i++){
            double tmp = 0;
            for(int j=0; j<=i; j++){
                tmp += var[j];
            }
            sum += tmp*tmp;
        }
        return sum;
    }

    public static double[] getFunc2Low(){
        return getFunc1Low();
    }

    public static double[] getFunc2up(){
        return getFunc1Up();
    }

    public static double Func3(double[] var){
        double sum=0;
        for(int i=0;i<var.length;i++)
        {
            sum+=(var[i]+0.5)*(var[i]+0.5);
        }
        return sum;
    }

    public static double[] getFunc3Low(){
        return getFunc1Low();
    }

    public static double[] getFunc3Up(){
        return getFunc1Up();
    }

    public static double Func4(double[] var){
        double y;
        if(var.length < 2){
            y = 9.42;
        }else{
            y = var[1];
        }
        return Math.pow((y - (5.1/(4*Math.PI*Math.PI))*var[0]*var[0] + (5/Math.PI)*var[0] - 6), 2) + 10 + 10*(1 - (1/(8*Math.PI))*Math.cos(var[0]));
    }

    public static double[] getFunc4Low(){
        double[] d = new double[2];
        d[0] = -5;
        d[1] = 0;
        return d;
    }

    public static double[] getFunc4Up(){
        double[] d = new double[2];
        d[0] = 10;
        d[1] = 15;
        return d;
    }

    public static double Func5(double[] var){
        double sum=0;
        for(int i=0;i<var.length-1;i++)
        {
            sum += 100*(var[i+1] - var[i]*var[i])*(var[i+1] - var[i]*var[i]) + (var[i]-1)*(var[i]-1);
        }
        return sum;
    }

    public static double[] getFunc5Low(){
        int dimension = 30;
        double[] d = new double[dimension];
        for(int j=0; j<dimension; j++){
            d[j] = -5;
        }
        return d;
    }

    public static double[] getFunc5Up(){
        int dimension = 30;
        double[] d = new double[dimension];
        for(int j=0; j<dimension; j++){
            d[j] = 10;
        }
        return d;
    }

    public static double Func6(double[] var){
        double y;
        int sum = 0;
        if(var.length < 2){
            y = -0.5471;
        }else{
            y = var[1];
        }
        sum += 1 + 2.5*y - 1.5*var[0];
        sum += Math.sin(var[0] + y);
        sum += (y - var[0])*(y - var[0]);
        return sum;
    }

    public static double[] getFunc6Low(){
        double[] d = new double[2];
        d[0] = -1.5;
        d[1] = -3;
        return d;
    }

    public static double[] getFunc6Up(){
        double[] d = new double[2];
        d[0] = 4;
        d[1] = 4;
        return d;
    }

    public static double Func7(double[] var){
        double y;
        int sum = 0;
        if(var.length < 2){
            y = 0.5;
        }else{
            y = var[1];
        }
        sum += Math.pow((1.5 - var[0] + var[0]*y), 2);
        sum += Math.pow((2.25 - var[0] + var[0]*y*y), 2);
        sum += Math.pow((2.625 - var[0] + var[0]*y*y*y), 2);
        return sum;
    }

    public static double[] getFunc7Low(){
        int dimension = 2;
        double[] d = new double[dimension];
        for(int j=0; j<dimension; j++){
            d[j] = -4.5;
        }
        return d;
    }

    public static double[] getFunc7Up(){
        int dimension = 2;
        double[] d = new double[dimension];
        for(int j=0; j<dimension; j++){
            d[j] = 4.5;
        }
        return d;
    }

    public static double Func8(double[] var){
        double y;
        int sum = 0;
        if(var.length < 2){
            y = 1;
        }else{
            y = var[1];
        }
        sum += 100*Math.sqrt(Math.abs(y - 0.01*var[0]*var[0]));
        sum += 0.01*Math.abs(var[0]+10);
        return sum;
    }

    public static double[] getFunc8Low(){
        double[] d = new double[2];
        d[0] = -15;
        d[1] = -3;
        return d;
    }

    public static double[] getFunc8Up(){
        double[] d = new double[2];
        d[0] = -5;
        d[1] = 3;
        return d;
    }

    public static double Func9(double[] var){
        double sum=0;
        for(int i=0;i<var.length;i++)
        {
            sum += -var[i]* Math.sin(Math.sqrt(Math.abs(var[i])));
        }
        return sum;
    }

    public static double[] getFunc9Low(){
        int dimension = 30;
        double[] d = new double[dimension];
        for(int j=0; j<dimension; j++){
            d[j] = -500;
        }
        return d;
    }

    public static double[] getFunc9Up(){
        int dimension = 30;
        double[] d = new double[dimension];
        for(int j=0; j<dimension; j++){
            d[j] = 500;
        }
        return d;
    }

    public static double Func10(double[] var){
        double sum=0;
        for(int i=0;i<var.length;i++)
        {
            sum += 10 + var[i]*var[i] - 10*Math.cos(2*Math.PI*var[i]);
        }
        return sum;
    }

    public static double[] getFunc10Low(){
        int dimension = 30;
        double[] d = new double[dimension];
        for(int j=0; j<dimension; j++){
            d[j] = -5.12;
        }
        return d;
    }

    public static double[] getFunc10Up(){
        int dimension = 30;
        double[] d = new double[dimension];
        for(int j=0; j<dimension; j++){
            d[j] = 5.12;
        }
        return d;
    }

    public static double Func11(double[] var){
        double sum=0;
        for(int i=0;i<var.length;i++)
        {
            double y = var[i];
            if(Math.abs(var[i]) < 0.5){
                y = var[i];
            }else{
                y = Math.round(2*y)/2.0;
            }
            sum += y*y - 10*Math.cos(2*Math.PI*y) + 10;
        }
        return sum;
    }

    public static double[] getFunc11Low(){
        return getFunc10Low();
    }

    public static double[] getFunc11Up(){
        return getFunc10Up();
    }

    public static double Func12(double[] var){
        double sum=0;
        sum += -20*Math.exp(-0.2*Math.sqrt((1.0/var.length)*Func1(var)));
        double tmp = 0;
        for(int i=0;i<var.length;i++)
        {
            tmp += Math.cos(2*Math.PI*var[i]);
        }
        sum += -Math.exp((1.0/var.length)*tmp+20);
        return sum;
    }

    public static double[] getFunc12Low(){
        int dimension = 30;
        double[] d = new double[dimension];
        for(int j=0; j<dimension; j++){
            d[j] = -32;
        }
        return d;
    }

    public static double[] getFunc12Up(){
        int dimension = 30;
        double[] d = new double[dimension];
        for(int j=0; j<dimension; j++){
            d[j] = 32;
        }
        return d;
    }

    public static double Func13(double[] var){
        double sum=0;
        sum += Func1(var)/4000;
        sum += 1;
        double multi = 1;
        for(int i=0;i<var.length;i++)
        {
            multi *= Math.cos(var[i]/Math.sqrt(i+1.0));
        }
        sum += -multi;
        return sum;
    }

    public static double[] getFunc13Low(){
        int dimension = 30;
        double[] d = new double[dimension];
        for(int j=0; j<dimension; j++){
            d[j] = -600;
        }
        return d;
    }

    public static double[] getFunc13Up(){
        int dimension = 30;
        double[] d = new double[dimension];
        for(int j=0; j<dimension; j++){
            d[j] = 600;
        }
        return d;
    }
}
