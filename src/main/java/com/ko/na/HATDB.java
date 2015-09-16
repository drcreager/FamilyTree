package com.ko.na;

import java.awt.print.*;
import java.awt.*;
import javax.swing.*;

public class HATDB extends javax.swing.JFrame implements Printable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2529543975193795716L;
	JFrame frameToPrint;

    /** Creates new form HATDB */
    public HATDB() {
    }

    @Override
    public int print(Graphics g, PageFormat pf, int page) throws
        PrinterException {

        if (page > 0) { /* We have only one page, and 'page' is zero-based */
            return NO_SUCH_PAGE;
        }

        /* User (0,0) is typically outside the imageable area, so we must
         * translate by the X and Y values in the PageFormat to avoid clipping
         */
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        /* Now print the window and its visible contents */
        frameToPrint.printAll(g);

        /* tell the caller that this page is part of the printed document */
        return PAGE_EXISTS;
    }

    public HATDB(JFrame f) {
        frameToPrint = f;
    }

    @SuppressWarnings("unused")
	private void OK_ButtonActionPerformed(java.awt.event.ActionEvent evt) {
        PrinterJob job = PrinterJob.getPrinterJob();
        //job.setPrintable();
        boolean ok = job.printDialog();
        if (ok) {
            try {
                job.print();
            } catch (PrinterException ex) {
                ex.printStackTrace(System.err);
            }
        }

    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */

        @SuppressWarnings("unused")
		JFrame f = new JFrame("Print UI Example");

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new HATDB().setVisible(true);
            }
        });
    }
}