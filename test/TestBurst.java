/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import irc.Sentence;
import irc.SentenceInterface;
import java.util.Random;
import jvn.JvnProxy;

/**
 *
 * @author Baptiste
 */
public class TestBurst {
    SentenceInterface sentence;

	public static void main(String argv[]) {

            try {
                SentenceInterface sentence = (SentenceInterface) JvnProxy.newInstance("sentence");

                if (sentence == null) {
                    System.out.println("Création d'un objet Sentence..");
                    sentence = (SentenceInterface) JvnProxy.newInstance("sentence", new Sentence());
                    System.out.println("Création terminée.");
                }

                Random r = new Random();

                while (true) {
                    if (r.nextInt(2) == 0) {
                        sentence.read();
                    } else {
                        sentence.write("message");
                    }
                }

            } catch (Exception e1) {
                e1.printStackTrace();
            }

	}
}
