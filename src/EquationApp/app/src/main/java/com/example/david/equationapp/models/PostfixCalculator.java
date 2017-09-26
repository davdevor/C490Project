package com.example.david.equationapp.models;

import java.util.ArrayList;
import java.util.Stack;
public class PostfixCalculator {

    final String operands = "^*+-/" ;
    double result;

    final static int PRECEDENCE_PLUS=  1;
    final static int PRECEDENCE_MINUS=  1;
    final static int PRECEDENCE_MULTIPLIY=  2;
    final static int PRECEDENCE_DIVIDE=  2;
    final static int PRECEDENCE_EXPONENT=  3;
    final static int PRECEDENCE_PARANTHESIS=  4;

    public PostfixCalculator(){

    }

    public int operatorToPrecedence(String op){
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
        else
            return PostfixCalculator.PRECEDENCE_PARANTHESIS;
    }

    public boolean isOperand(String s, boolean allowParanethesis){
        s = s.trim();
        if (s.length() != 1 )
            return false;
        if (allowParanethesis &&  ( s.equals("(") ||  s.equals(")") ) )
            return true;
        else return 	operands.indexOf( s ) != -1 ;
    }

    public boolean isNumber(String s){
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

    public void parseRPN(String input){

        String rpnStr = input;
        String[] tokens = rpnStr.split("\\s+");//remove all white space
        Stack<Double> numberStack =new Stack<Double>();

        boolean  bAllowParenthesis = false;
        for( String token : tokens)
        {
            if(token.equals("-")==false && isNumber(token ))
            {
                double d = Double.parseDouble( token  ) ;
                numberStack.push(d ) ;
            }
            else if( isOperand( token , bAllowParenthesis   ) )
            {
                if( numberStack.size() <  2 )
                {
                    System.out.println("Invalid Syntax, operator " + token + " must be preceeded by at least two operands");
                    return;
                }
                double num1 = numberStack.pop();
                double num2 = numberStack.pop() ;
                double result = this.calculate( num2 , num1 , token  ) ;
                numberStack.push( result);
            }
            else if( token.trim().length() > 0 )
                System.out.println( token + " is invalid, only use numbers or operators " );
        }
        result= numberStack.pop();
    }

    double getResult() {
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

    public ArrayList<String> infixToPostfix(String input){

        ArrayList<String> output  = new ArrayList<String>();
        input = input.replaceAll("\\s+","") ;

        Stack<String> operandStack = new Stack<String>();
        for(int i = 0 ;i< input.length() ; i++)
        {
            String currentToken = input.substring(i,i+1);
            if( isOperand(currentToken, true))
            {
                //this.p("I currentToken : " + currentToken + " , operands : " + operands +",output: "+ output.toString().replaceAll(",", " " ) );
                if( operandStack.size() == 0)
                    operandStack.push( currentToken );
                else if(operandStack.size() > 0  && currentToken.equals(")" ) )
                {
                    //this.p("I, currentToken " +currentToken+ ",  operands : " + operands  );
                    while( operandStack.size() > 0  && operandStack.peek().equals("(" ) == false)
                    {
                        output.add( operandStack.pop() ) ;
                    }
                    ///if(operands.size() > 0) this.p("II now get rid of closing paran : " + operands.peek() );
                    operandStack.pop(); // remove "(" that matches opening parenthesis
                }
                else if(operandStack.size() > 0 )
                {
                    if( (currentToken.equals("(") && operandStack.peek().equals("(") ) || (currentToken.equals("(")== false &&  this.operatorToPrecedence( operandStack.peek() ) >= this.operatorToPrecedence(currentToken) ) )
                    {
                        while (operandStack.size()> 0 && operandStack.peek().equals("(")== false &&  this.operatorToPrecedence( operandStack.peek() ) >= this.operatorToPrecedence(currentToken))
                        {
                            //this.p("III pop off " + operands.peek() +", greater than " + currentToken +",current  out: "+ output.toString().replaceAll(",", " " ) );
                            output.add(operandStack.pop() );
                        }
                        operandStack.push( currentToken ) ;
                    }
                    else if( this.operatorToPrecedence( operandStack.peek() ) < this.operatorToPrecedence(currentToken) )
                    {
                        operandStack.push( currentToken ) ;
                    }
                }
            }
            else if (    isNumber( currentToken ) )
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
        return output;
    }




    public static void main(String[] args){

        boolean debug = false;
        PostfixCalculator rp = new PostfixCalculator();
        ArrayList<String> asPostfix ;
        String parseMe =" (((3 - 5)*(2)) + 6) ";

        String answer = "3 5 - ^ 2 6 + ";
        System.out.println("parseMe " +parseMe) ;
        asPostfix =  rp.infixToPostfix(parseMe);
        String asPostfix_str = asPostfix.toString().replaceAll(",", " " ) ;
        asPostfix_str = asPostfix_str.substring(1, asPostfix_str.length()-1 ) ;

        System.out.println("asPostfix " +  asPostfix_str);

        System.out.println("----------now test out the RPN parser----------");

        rp.parseRPN( asPostfix_str ) ;
        System.out.println( parseMe + " =  " + rp.getResult() ) ;

    }
}

