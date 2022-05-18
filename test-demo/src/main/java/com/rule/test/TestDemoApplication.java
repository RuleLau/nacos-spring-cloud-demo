package com.rule.test;

import com.custom.configuration.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayDeque;
import java.util.Deque;

@SpringBootApplication
public class TestDemoApplication {

    @Autowired
    JSONUtil jsonUtil;

//	public static void main(String[] args) {
//		SpringApplication.run(TestDemoApplication.class, args);
//	}

    /**
     * 输入N，输出N对括号的正确组合。
     * 比如输入1，输出()
     * 输入2，输出()()、(())
     * 3 ()()() ((())) ()
     *
     * @param args
     */
    public static void main(String[] args) {
        Deque<String> print = print(3);
        while (!print.isEmpty()) {
            System.out.println(print.pop());
        }
    }

    public static Deque<String> print(int n) {
        Deque<String> stack = new ArrayDeque<>();
        if (n == 1) {
            stack.push("()");
            return stack;
        }
        Deque<String> temp = print(n - 1);
        while (!temp.isEmpty()) {
            String pop = temp.pop();
            stack.push("(" + pop + ")");
            String left = "()" + pop;
            String right = pop + "()";
            stack.push(left);
            if (!left.equals(right)) {
                stack.push(right);
            }
        }
        return stack;
    }
}
