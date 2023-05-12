package com.craftinginterpreters.lox;

import com.craftinginterpreters.lox.Expr.Binary;
import com.craftinginterpreters.lox.Expr.Grouping;
import com.craftinginterpreters.lox.Expr.Literal;
import com.craftinginterpreters.lox.Expr.Unary;

public class RpnPrinter implements Expr.Visitor<String> {

    public static void main(String[] args) {

        Expr add = new Expr.Binary(new Expr.Literal(1), new Token(TokenType.PLUS, "+", null, 1), new Expr.Literal(2));
        Expr groupAdd = new Expr.Grouping(add);
        Expr sub = new Expr.Binary(new Expr.Literal(4), new Token(TokenType.MINUS, "-", null, 1), new Expr.Literal(3));
        Expr groupSub = new Expr.Grouping(sub);
        Expr mult = new Expr.Binary(groupAdd, new Token(TokenType.STAR, "*", null, 1), groupSub);

        Expr expression = new Expr.Binary(
                new Expr.Unary(
                        new Token(TokenType.MINUS, "-", null, 1),
                        new Expr.Literal(123)),
                new Token(TokenType.STAR, "*", null, 1),
                new Expr.Grouping(
                        new Expr.Literal("str")));
        System.out.println(new RpnPrinter().print(mult));
        System.out.println(new RpnPrinter().print(expression));

    }

    private String print(Expr expression) {
        String ret = expression.accept(this);
        return ret;
    }

    @Override
    public String visitBinaryExpr(Binary expr) {
        String left = expr.left.accept(this);
        String right = expr.right.accept(this);
        String operator = expr.operator.lexeme;
        return left + " " + right + " " + operator;
    }

    @Override
    public String visitGroupingExpr(Grouping expr) {
        Expr e = expr.expression;
        return e.accept(this);
    }

    @Override
    public String visitLiteralExpr(Literal expr) {
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Unary expr) {
        String operator = expr.operator.lexeme;
        String right = expr.right.accept(this);
        if (operator == "-") {
            operator = "~";
        }
        return right + operator;
    }

}
