package com.example.david.equationapp.models;

import java.util.ArrayList;
import java.util.Stack;
public class PostfixCalculator {

    private final String operands = "^*+-/_()" ;
    private double result;
    private String equation;
    private ArrayList<String> vars = new ArrayList<>();
    private ArrayList<String> varsValue;

    private final static int PRECEDENCE_PLUS=  1;
    private final static int PRECEDENCE_MINUS=  1;
    private final static int PRECEDENCE_MULTIPLIY=  2;
    private final static int PRECEDENCE_DIVIDE=  2;
    private final static int PRECEDENCE_EXPONENT=  3;
    private final static int PRECEDENCE_PARANTHESIS=  4;
    private final static int PRECENDENCE_NEGATIVE =5;

    public PostfixCalculator(String s,ArrayList<String> varsValue){
        equation = s;
        this.varsValue = varsValue;
        getVars();
        update();
        infixToPostfix();
        parseRPN();
    }
    public void setVarValue(ArrayList<String> v){
        varsValue = v;
    }
    private void getVars() {
        equation = equation.replaceAll("\\s+", "");
        int length = equation.length();
        for (int i = 0; i < length; i++) {
            char c = equation.charAt(i);
            StringBuilder temp = new StringBuilder();
            while (!operands.contains(Character.toString(c)) && i < length ) {
                temp.append(c);
                try {
                    c = equation.charAt(++i);
                }
                catch(StringIndexOutOfBoundsException e){
                    i = length;
                }
            }
            if(temp.length()!=0)
                vars.add(temp.toString());
        }
    }
    private void update() {
        int length = vars.size();
        for(int i = 0; i < length; i++){
            equation = equation.replace(vars.get(i), varsValue.get(i));
        }
    }

    private int operatorToPrecedence(String op){
        if( op.equals("+"))
            return PostfixCalculator.PRECEDENCE_PLUS ;
        else if( op.equals("-"))
            return PostfixCalculator.PRECEDENCE_MINUS;
        else if( op.equals("*"))
            return PostfixCalculator.PRECEDENCE_MULTIPLIY ;
        else if( op.equals("^"))
            return PostfixCalculator.PRECEDENCE_EXPONENT ;
        else if( op.equals("/") )
            return PostfixCalculator.PRECEDENCE_DIVIDE ;
        else if (op.equals("(")||op.equals(")"))
            return PostfixCalculator.PRECEDENCE_PARANTHESIS;
        else
            return PostfixCalculator.PRECENDENCE_NEGATIVE;
    }

    private boolean isOperand(String s, boolean allowParanethesis){
        s = s.trim();
        if (s.length() != 1 )
            return false;
        if (allowParanethesis &&  ( s.equals("(") ||  s.equals(")") ) )
            return true;
        else return 	operands.indexOf( s ) != -1 ;
    }

    private boolean isNumber(String s){
        String  master="-0123456789.";
        s = s.trim();

        for( int i = 0;i < s.length()  ;i++)
        {
            String lttr = s.substring(i, i+1);
            if(master.indexOf( lttr) == -1)
                return false;
        }
        return true ;
    }

    private void parseRPN(){
        String rpnStr = equation;
        String[] tokens = rpnStr.split("\\s+");//remove all white space

        Stack<Double> numberStack =new Stack<Double>();

        boolean  bAllowParenthesis = false;
        for( String token : tokens)
        {
            if(!token.equals("-") && isNumber(token ))
            {
                double d = Double.parseDouble( token  ) ;
                numberStack.push(d) ;
            }
            else if( isOperand( token , bAllowParenthesis   ) ) {
                if (token.equals("_")) {
                    double num1 = numberStack.pop();
                    numberStack.push(-1.0 * num1);
                } else {
                    if (numberStack.size() < 2) {

                        System.out.println("Invalid Syntax, operator " + token + " must be preceeded by at least two operands");
                        return;
                    }
                    double num1 = numberStack.pop();
                    double num2 = numberStack.pop();
                    double result = this.calculate(num2, num1, token);
                    numberStack.push(result);
                }
            }
            else if( token.trim().length() > 0 )
                System.out.println( token + " is invalid, only use numbers or operators " );
        }
        result= numberStack.pop();
    }

    public double getResult() {
        return result;
    }

    private Double calculate(double num1, double num2, String op ) {
        if( op.equals("+"))
            return num1 + num2;
        else if( op.equals("-"))
            return num1 - num2;
        else if( op.equals("*"))
            return num1 * num2;
        else if( op.equals("^"))
            return Math.pow(num1 , num2 );
        else if( op.equals("/") )
        {
            if(num2 ==0 )
                throw new ArithmeticException("Division by zero!");
            return num1 / num2;
        }
        else
        {
            System.out.println(op + " is not a supported operand") ;
            return null;
        }
    }

    private void infixToPostfix(){
        String input = equation;
        ArrayList<String> output  = new ArrayList<String>();
        String temp = "(";
        temp += input;
        temp+=")";
        input = temp;
        input = input.replaceAll("\\s+","");
        Stack<String> operandStack = new Stack<String>();
        for(int i = 0 ;i< input.length() ; i++)
        {
            String currentToken = input.substring(i,i+1);
            if( isOperand(currentToken, true)) {
                //this if is for negative numbers
                if (currentToken.equals("-")&&isOperand(Character.toString(input.charAt(i - 1)), true)) {
                    operandStack.push("_");
                }
                else {
                    //this.p("I currentToken : " + currentToken + " , operands : " + operands +",output: "+ output.toString().replaceAll(",", " " ) );
                    if (operandStack.size() == 0)
                        operandStack.push(currentToken);
                    else if (operandStack.size() > 0 && currentToken.equals(")")) {
                        //this.p("I, currentToken " +currentToken+ ",  operands : " + operands  );
                        while (operandStack.size() > 0 && operandStack.peek().equals("(") == false) {
                            output.add(operandStack.pop());
                        }
                        ///if(operands.size() > 0) this.p("II now get rid of closing paran : " + operands.peek() );
                        operandStack.pop(); // remove "(" that matches opening parenthesis
                    } else if (operandStack.size() > 0) {
                        if ((currentToken.equals("(") && operandStack.peek().equals("(")) || (currentToken.equals("(") == false && this.operatorToPrecedence(operandStack.peek()) >= this.operatorToPrecedence(currentToken))) {
                            while (operandStack.size() > 0 && operandStack.peek().equals("(") == false && this.operatorToPrecedence(operandStack.peek()) >= this.operatorToPrecedence(currentToken)) {
                                //this.p("III pop off " + operands.peek() +", greater than " + currentToken +",current  out: "+ output.toString().replaceAll(",", " " ) );
                                output.add(operandStack.pop());
                            }
                            operandStack.push(currentToken);
                        } else if (this.operatorToPrecedence(operandStack.peek()) < this.operatorToPrecedence(currentToken)) {
                            operandStack.push(currentToken);
                        }
                    }
                }
            }
            else if(isNumber( currentToken ))
            {
                // need a while loop to keep concatenating all numbers into one string that we end at end of while loop
                numberLoop : while(  i+1 < input.length())
                {
                    String nxtLttr =  input.substring(i+1,i+2);
                    if(nxtLttr.equals("-") ) //then it's a subtraction sign not a unary negative sign
                        break numberLoop;
                    if( isNumber(nxtLttr ) )
                    {
                        currentToken +=nxtLttr;
                        i++;
                    }
                    else
                        break numberLoop;
                }
                try{//in case it is only dots and or negative signs
                    output.add(currentToken) ;
                }
                catch (NumberFormatException e){  System.out.println(currentToken + " is not a valid number"); }
            }
//			this.p("isNumber() , currentToken: " + currentToken + " , operands : " + operands );
        }
        while( operandStack.size() > 0 )
        {
            output.add( operandStack.pop() ) ;
        }
        String asPostfix_str = output.toString().replaceAll(",", " " ) ;
        equation = asPostfix_str.substring(1, asPostfix_str.length()-1 ) ;
    }




   /*public static void main(String[] args){

        boolean debug = false;
        String parseMe ="(x*(y - z))^2";
        String [] temp = new String[]{"-1","-5","-3","2"};
        ArrayList<String> varsValue = new ArrayList<String>();
        for (String x : temp) {
          varsValue.add(x);
    }

        PostfixCalculator rp = new PostfixCalculator(parseMe,varsValue);
        ArrayList<String> asPostfix ;


        String answer = "3 5 - ^ 2 6 + ";
        System.out.println("parseMe " +parseMe) ;

        (for (String x:asPostfix) {
            System.out.print(x);
        }
        System.out.println();


        System.out.println("asPostfix " +  asPostfix_str);

        System.out.println("----------now test out the RPN parser----------");

        rp.parseRPN() ;
        System.out.println( parseMe + " =  " + rp.getResult() ) ;

    }*/
}

