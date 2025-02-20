/*
 * "Concave" hulls by Glenn Hudson and Matt Duckham
 *
 * Source code downloaded from https://archive.md/l3Un5#selection-571.0-587.218 on 3rd November 2021.
 *
 * - This software is Copyright (C) 2008 Glenn Hudson released under Gnu Public License (GPL). Under 
 *   GPL you are free to use, modify, and redistribute the software. Please acknowledge Glenn Hudson 
 *   and Matt Duckham as the source of this software if you do use or adapt the code in further research 
 *   or other work. For full details of GPL see http://www.gnu.org/licenses/gpl-3.0.txt.
 * - This software comes with no warranty of any kind, expressed or implied.
 * 
 * A paper with full details of the characteristic hulls algorithm is published in Pattern Recognition.
 * Duckham, M., Kulik, L., Worboys, M.F., Galton, A. (2008) Efficient generation of simple polygons for
 * characterizing the shape of a set of points in the plane. Pattern Recognition v41, 3224-3236
 *
 * The software was developed by Glenn Hudson while working with me as an RA. The characteristic shapes 
 * algorithm is collaborative work between Matt Duckham, Lars Kulik, Antony Galton, and Mike Worboys.
 * 
 */

package signalprocesser.voronoi.statusstructure.binarysearchtreeimpl.debug;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JPanel;

import signalprocesser.shared.TextToolkit;
import signalprocesser.voronoi.VPoint;
import signalprocesser.voronoi.VoronoiShared;
import signalprocesser.voronoi.eventqueue.VSiteEvent;
import signalprocesser.voronoi.statusstructure.binarysearchtreeimpl.VInternalNode;
import signalprocesser.voronoi.statusstructure.binarysearchtreeimpl.VLeafNode;
import signalprocesser.voronoi.statusstructure.binarysearchtreeimpl.VNode;

public class DebugTree extends javax.swing.JDialog {
    
    public static final int BOX_WIDTH = 120;
    public static final int BOX_HEIGHT = 50;
    public static final int GAP_BETWEENBOXES = 20;
    public static final Font BOX_FONT = new Font("Arial",Font.BOLD,9);
    
    private int sweepline = -1;
    private VNode rootnode = null;
    private TreePanel panel = null;
    
    public DebugTree(java.awt.Frame parent) {
        super(parent, false);
        initComponents();
        setTitle("Tree Display App");
        panel = new TreePanel();
        this.getContentPane().add( panel , BorderLayout.CENTER );
    }
    
    public void setRootNode(VNode _rootnode, int _sweepline) {
        this.rootnode = _rootnode;
        this.sweepline = _sweepline;
        this.repaint();
    }
    
    public class TreePanel extends JPanel {
        
        public TreePanel() { }
        
        public void paintComponent(Graphics _g) {
            Graphics2D g = (Graphics2D) _g;
            
            // Get dimensions
            double width = this.getWidth();
            double height = this.getHeight();
            
            // Set background color
            g.setColor(Color.white);
            g.fillRect(0,0,(int)width,(int)height);
            
            // Set sweepline number
            g.setColor(Color.black);
            g.drawString("sweepline=" + sweepline, 20, 20);
            
            drawNode( g , rootnode , 0 , 0 , (int)width );
        }
        
        public void drawNode( Graphics2D g , VNode currnode , int depth , int left , int right ) {
            // Create text for node
            String text;
            if ( currnode==null ) {
                text = "NULL VALUE";
            } else if ( currnode.isInternalNode() ) {
                VInternalNode internalnode = (VInternalNode) currnode;
                text = internalnode.id + " (" + internalnode.v1.getX() + "," + internalnode.v1.getY() + ") & (" + internalnode.v2.getX() + "," + internalnode.v2.getY() + ")";

                if ( internalnode.getDepth()!=depth+1 ) {
                    throw new RuntimeException("Part of tree not equal to expected depth; expected=" + (depth+1) + ", actual=" + internalnode.getDepth());
                }
                
                VSiteEvent v1 = internalnode.v1;
                VSiteEvent v2 = internalnode.v2;
                
                // Calculate a, b and c of the parabola
                v1.calcParabolaConstants(sweepline);
                v2.calcParabolaConstants(sweepline);
                
                // Determine where two parabola meet
                double intersects[] = VoronoiShared.solveQuadratic(v2.a-v1.a, v2.b-v1.b, v2.c-v1.c);
                text += " = " + (int)intersects[0];
            } else {
                VLeafNode leafnode = (VLeafNode) currnode;
                text = "id #" + leafnode.siteevent.id + " (" + leafnode.siteevent.getX() + "," + leafnode.siteevent.getY() + ")";
            }
            
            // Draw current node
            int center = left + (right-left)/2;
            Rectangle rectangle = new Rectangle( center-BOX_WIDTH/2, GAP_BETWEENBOXES + (BOX_HEIGHT+GAP_BETWEENBOXES)*depth, BOX_WIDTH, BOX_HEIGHT );
            g.setColor(new Color(240,255,235));
            g.fillRect( rectangle.x , rectangle.y , rectangle.width , rectangle.height );
            if ( currnode==null ) {
                g.setColor(Color.yellow);
            } else if ( currnode.isInternalNode() ) {
                g.setColor(Color.red);
            } else {
                g.setColor(Color.blue);
            }
            g.drawRect( rectangle.x , rectangle.y , rectangle.width , rectangle.height );
            TextToolkit.writeFromTop(g, BOX_FONT, Color.BLACK, text, rectangle);
            
            // Draw child nodes
            if ( currnode!=null && currnode.isInternalNode() ) {
                VInternalNode internalnode = (VInternalNode) currnode;
                drawNode( g , internalnode.getLeft()  , depth+1 , left   , center );
                drawNode( g , internalnode.getRight() , depth+1 , center , right  );
            }
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-670)/2, (screenSize.height-569)/2, 670, 569);
    }
    // </editor-fold>//GEN-END:initComponents
    
    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        //System.exit(0);
    }//GEN-LAST:event_formWindowClosed
    
    public static void main(String args[]) {
        DebugTree app = new DebugTree(new javax.swing.JFrame());
        app.setVisible(true);
        VInternalNode node = new VInternalNode();
        VLeafNode leaf1 = new VLeafNode(new VSiteEvent(new VPoint(1,2)));
        VLeafNode leaf2 = new VLeafNode(new VSiteEvent(new VPoint(1,2)));
        node.setLeft(leaf1);
        node.setRight(leaf2);
        app.setRootNode(node, 10);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}
