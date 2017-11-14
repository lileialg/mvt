package com.mercator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

public class CoordinateMove {
	
	public static class LinkInfo{
		
		private int linkpid;
		
		private int meshid;
		
		private int sid;
		
		private int eid;
		
		private int direct;
		
		private Geometry geometry;
		
		private int code;
		
		private int rank;
		
		private String unid;
		
		private double[][] cs;
		
		public LinkInfo(int linkpid,int meshid,int sid,int eid,int direct,Geometry geometry,int code,int rank){
			this.linkpid = linkpid;
			this.meshid = meshid;
			this.direct = direct;
			if (this.direct == 2){
				this.sid = sid;
				this.eid = eid;
			}else{
				this.sid = eid;
				this.eid = sid;
			}
			
			this.geometry = geometry;
			this.code = code;
			this.rank = rank;
			
			this.unid = this.meshid+"_"+this.code+"_"+this.rank;
			
			cs = new double[geometry.getCoordinates().length][];
			
			Coordinate[] coordinates = this.geometry.getCoordinates();
			
			for(int i=0;i<cs.length;i++	){
				Coordinate c= coordinates[i];
				cs[i] = new double[]{c.x,c.y};
			}
				
		}

		public int getLinkpid() {
			return linkpid;
		}

		public int getMeshid() {
			return meshid;
		}

		public int getSid() {
			return sid;
		}

		public int getEid() {
			return eid;
		}

		public int getDirect() {
			return direct;
		}

		public int getCode() {
			return code;
		}

		public int getRank() {
			return rank;
		}

		public String getUnid() {
			return unid;
		}

		public double[][] getCs() {
			return cs;
		}
		
		
		

		private boolean isSV;
		
		public boolean isSV() {
			return isSV;
		}

		public void setSV(boolean isSV) {
			this.isSV = isSV;
		}

		public boolean isEV() {
			return isEV;
		}

		public void setEV(boolean isEV) {
			this.isEV = isEV;
		}



		private boolean isEV;
		
		private double startK;
		
		private double startB;
		
		private double endK;
		
		private double endB;
		
		private double[][] newCS;
		
		
		

		public double getStartK() {
			return startK;
		}

		public void setStartK(double startK) {
			this.startK = startK;
		}

		public double getStartB() {
			return startB;
		}

		public void setStartB(double startB) {
			this.startB = startB;
		}

		public double getEndK() {
			return endK;
		}

		public void setEndK(double endK) {
			this.endK = endK;
		}

		public double getEndB() {
			return endB;
		}

		public void setEndB(double endB) {
			this.endB = endB;
		}

		public double[][] getNewCS() {
			return newCS;
		}

		public void setNewCS(double[][] newCS) {
			this.newCS = newCS;
		}

	}
	
	
	
	

	public static void main(String[] args) {
		
		double[][] cs = new double[][]{{116.00000,39.00000},{116.0012,39.0013},{116.0022,39.00135}};
		
//		double[][] cs = new double[][]{{2,2},{3,1}};
		
//		double[][] cs = new double[][]{{2,3},{3,4}};
		
//		double[][] cs = new double[][]{{116.0012,39.0013},{116.0022,39.00135}};
		
//		double[][] cs = new double[][]{{116.00000,39.00000},{116.0012,39.0013}};
		
		int direct = 1;
		
		double moveStep = 0.00025;
		
//		double moveStep = Math.sqrt(2);
		
		CoordinateMove cm = new CoordinateMove(moveStep);
		
		double[][][] result = cm.move(cs, direct);
		
		System.out.print(result[0][0][0]+" "+result[0][0][1]+",");
		System.out.print(result[0][1][0]+" "+result[0][1][1]+",");
		System.out.println(result[0][2][0]+" "+result[0][2][1]);
		
		System.out.print(result[1][0][0]+" "+result[1][0][1]+",");
		System.out.print(result[1][1][0]+" "+result[1][1][1]+",");
		System.out.println(result[1][2][0]+" "+result[1][2][1]);
	}

	public static class Tupple {

		private double startX;

		private double startY;

		private double endX;

		private double endY;

		private double k;

		private double b;

		private boolean isV = false;

		public boolean isV() {
			return isV;
		}

		public void setV(boolean isV) {
			this.isV = isV;
		}

		public double getStartX() {
			return startX;
		}

		public void setStartX(double startX) {
			this.startX = startX;
		}

		public double getStartY() {
			return startY;
		}

		public void setStartY(double startY) {
			this.startY = startY;
		}

		public double getEndX() {
			return endX;
		}

		public void setEndX(double endX) {
			this.endX = endX;
		}

		public double getEndY() {
			return endY;
		}

		public void setEndY(double endY) {
			this.endY = endY;
		}

		public double getK() {
			return k;
		}

		public void setK(double k) {
			this.k = k;
		}

		public double getB() {
			return b;
		}

		public void setB(double b) {
			this.b = b;
		}

	}

	private double moveStep;

	public CoordinateMove(double moveStep) {
		this.moveStep = moveStep;
	}
	
	
	public static void batch(List<LinkInfo> list){
		
		Map<String,List<LinkInfo>> map = new HashMap<String,List<LinkInfo>>();
		
		for(LinkInfo li:list){
			if (!map.containsKey(li.getUnid())){
				map.put(li.getUnid(), new ArrayList<LinkInfo>());
			}
			
			map.get(li.getUnid()).add(li);
		}
		
		for(List<LinkInfo> lis : map.values()){
			Map<Integer,LinkInfo> smap = new HashMap<Integer,LinkInfo>();
			Map<Integer,LinkInfo> emap = new HashMap<Integer,LinkInfo>();
			
			 
			
			for(LinkInfo li: lis){
				smap.put(li.getSid(), li);
				emap.put(li.getEid(), li);
			}
			
			Iterator<Integer> itKey = emap.keySet().iterator();
			
			while(itKey.hasNext()){
				int enodeid = itKey.next();
				
				if (smap.containsKey(enodeid)){
					LinkInfo preLink = emap.get(enodeid);
					LinkInfo nextLink = smap.get(enodeid);
					
					if (preLink.getLinkpid() == nextLink.getLinkpid()) continue;
					
//					if (preLink.getUnid().equals("595673_241_3") && preLink.getLinkpid()==12961970){
//						System.out.println(nextLink.getLinkpid());
//					}
					
					double[][] lastCS = preLink.getNewCS();
					
					double lastPoint[] = lastCS[lastCS.length-1];
					
					double[][] nextCS = nextLink.getNewCS();
					
					double firstPoint[] = nextCS[0];
					
					firstPoint[0] = lastPoint[0];
					firstPoint[1] = lastPoint[1];
					
//					if (preLink.isEV() || nextLink.isSV()){
////						
////						if (preLink.isEV() && nextLink.isSV()) continue;
////						
////						
////						
////						if (preLink.isEV()){
////							
////							double newY = nextLink.getStartK() * lastPoint[0] + nextLink.getStartB();
////							
////							lastPoint[1] = newY;
////							
////							firstPoint[0] = lastPoint[0];
////							
////							firstPoint[1] = newY;
////							
////						}else{
////							
////							double newY = preLink.getEndK() * firstPoint[0] + preLink.getEndB();
////							
////							lastPoint[0] = firstPoint[0];
////							
////							lastPoint[1] = newY;
////							
////							firstPoint[1] = newY;
////							
////						}
//						
//					}else{
//						
////						if (Math.abs(preLink.getEndK() - nextLink.getStartK())>)
//						
////						double newX = (nextLink.getStartB() - preLink.getEndB()) /(preLink.getEndK() - nextLink.getStartK());
////						
////						double newY = newX * preLink.getEndK() + preLink.getEndB();
////						
////						lastPoint[0] = newX;
////						lastPoint[1] = newY;
////						
////						firstPoint[0] = newX;
////						firstPoint[1] = newY;
//						
//						
////						lastPoint[0] = newX;
////						lastPoint[1] = newY;
//						
//						firstPoint[0] = lastPoint[0];
//						firstPoint[1] = lastPoint[1];
////					
////						System.out.println("aaaa");
//					}
					
				}
			}
		}
		
	}
	
	
	public void move(LinkInfo li) {
		
		int direct = li.getDirect();
		
		double[][] cs = li.getCs();
		
//		double[][][] links = direct==1?new double[2][][]:new double[1][][];
		
		double[][] links = null;
		
//		if (direct == 1){
//			//处理顺向
//			Tupple[] tupples = new Tupple[cs.length-1];
//			for(int i=0;i<cs.length-1;i++){
//				tupples[i] = move(cs[i],cs[i+1]);
//			}
//			
//			intersectPoint(tupples);
//			
//			if (tupples.length == 1){
//				links[0] = new double[][]{{tupples[0].getStartX(),tupples[0].getStartY()},{tupples[0].getEndX(),tupples[0].getEndY()}};
//			}else{
//				double[][] vs = new double[tupples.length+1][];
//				
//				for(int i=0;i<tupples.length;i++){
//					vs[i] = new double[]{tupples[i].getStartX(),tupples[i].getStartY()};
//					
//				}
//				vs[vs.length-1] = new double[]{tupples[tupples.length-1].getEndX(),tupples[tupples.length-1].getEndY()};
//				
//				links[0] = vs;
//			}
//			
//			
//			//处理逆向
//			tupples = new Tupple[cs.length-1];
//			for(int i=0;i<cs.length-1;i++){
//				tupples[i] = move(cs[cs.length-1-i],cs[cs.length-1 - (i+1)]);
//			}
//			
//			intersectPoint(tupples);
//			
//			if (tupples.length == 1){
//				links[1] = new double[][]{{tupples[0].getStartX(),tupples[0].getStartY()},{tupples[0].getEndX(),tupples[0].getEndY()}};
//			}else{
//				double[][] vs = new double[tupples.length+1][];
//				
//				for(int i=0;i<tupples.length;i++){
//					vs[i] = new double[]{tupples[i].getStartX(),tupples[i].getStartY()};
//					
//				}
//				vs[vs.length-1] = new double[]{tupples[tupples.length-1].getEndX(),tupples[tupples.length-1].getEndY()};
//				
//				links[1] = vs;
//			}
//			
//		}else 
		
		Tupple[] tupples = new Tupple[cs.length-1];
			
		if (direct ==2){
			
			for(int i=0;i<cs.length-1;i++){
				tupples[i] = move(cs[i],cs[i+1]);
			}
			
			intersectPoint(tupples);
			
			if (tupples.length == 1){
				links = new double[][]{{tupples[0].getStartX(),tupples[0].getStartY()},{tupples[0].getEndX(),tupples[0].getEndY()}};
			}else{
				double[][] vs = new double[tupples.length+1][];
				
				for(int i=0;i<tupples.length;i++){
					vs[i] = new double[]{tupples[i].getStartX(),tupples[i].getStartY()};
					
				}
				vs[vs.length-1] = new double[]{tupples[tupples.length-1].getEndX(),tupples[tupples.length-1].getEndY()};
				
				links = vs;
			}
			
			
		}else{
			for(int i=0;i<cs.length-1;i++){
				tupples[i] = move(cs[cs.length-1-i],cs[cs.length-1 - (i+1)]);
			}
			
			intersectPoint(tupples);
			
			if (tupples.length == 1){
				links = new double[][]{{tupples[0].getStartX(),tupples[0].getStartY()},{tupples[0].getEndX(),tupples[0].getEndY()}};
			}else{
				double[][] vs = new double[tupples.length+1][];
				
				for(int i=0;i<tupples.length;i++){
					vs[i] = new double[]{tupples[i].getStartX(),tupples[i].getStartY()};
					
				}
				vs[vs.length-1] = new double[]{tupples[tupples.length-1].getEndX(),tupples[tupples.length-1].getEndY()};
				
				links = vs;
			}
		}
		
		li.setSV(tupples[0].isV());
		
		li.setStartK(tupples[0].getK());
		
		li.setStartB(tupples[0].getB());
		
		li.setEV(tupples[tupples.length-1].isV());
		
		li.setEndK(tupples[tupples.length-1].getK());
		
		li.setEndB(tupples[tupples.length-1].getB());
		
		li.setNewCS(links);
		
	}
	

	public double[][][] move(double[][] cs, int direct) {
		
		double[][][] links = direct==1?new double[2][][]:new double[1][][];
		
		if (direct == 1){
			//处理顺向
			Tupple[] tupples = new Tupple[cs.length-1];
			for(int i=0;i<cs.length-1;i++){
				tupples[i] = move(cs[i],cs[i+1]);
			}
			
			intersectPoint(tupples);
			
			if (tupples.length == 1){
				links[0] = new double[][]{{tupples[0].getStartX(),tupples[0].getStartY()},{tupples[0].getEndX(),tupples[0].getEndY()}};
			}else{
				double[][] vs = new double[tupples.length+1][];
				
				for(int i=0;i<tupples.length;i++){
					vs[i] = new double[]{tupples[i].getStartX(),tupples[i].getStartY()};
					
				}
				vs[vs.length-1] = new double[]{tupples[tupples.length-1].getEndX(),tupples[tupples.length-1].getEndY()};
				
				links[0] = vs;
			}
			
			
			//处理逆向
			tupples = new Tupple[cs.length-1];
			for(int i=0;i<cs.length-1;i++){
				tupples[i] = move(cs[cs.length-1-i],cs[cs.length-1 - (i+1)]);
			}
			
			intersectPoint(tupples);
			
			if (tupples.length == 1){
				links[1] = new double[][]{{tupples[0].getStartX(),tupples[0].getStartY()},{tupples[0].getEndX(),tupples[0].getEndY()}};
			}else{
				double[][] vs = new double[tupples.length+1][];
				
				for(int i=0;i<tupples.length;i++){
					vs[i] = new double[]{tupples[i].getStartX(),tupples[i].getStartY()};
					
				}
				vs[vs.length-1] = new double[]{tupples[tupples.length-1].getEndX(),tupples[tupples.length-1].getEndY()};
				
				links[1] = vs;
			}
			
		}else if (direct ==2){
			Tupple[] tupples = new Tupple[cs.length-1];
			for(int i=0;i<cs.length-1;i++){
				tupples[i] = move(cs[i],cs[i+1]);
			}
			
			intersectPoint(tupples);
			
			if (tupples.length == 1){
				links[0] = new double[][]{{tupples[0].getStartX(),tupples[0].getStartY()},{tupples[0].getEndX(),tupples[0].getEndY()}};
			}else{
				double[][] vs = new double[tupples.length+1][];
				
				for(int i=0;i<tupples.length;i++){
					vs[i] = new double[]{tupples[i].getStartX(),tupples[i].getStartY()};
					
				}
				vs[vs.length-1] = new double[]{tupples[tupples.length-1].getEndX(),tupples[tupples.length-1].getEndY()};
				
				links[0] = vs;
			}
		}else{
			Tupple[] tupples = new Tupple[cs.length-1];
			for(int i=0;i<cs.length-1;i++){
				tupples[i] = move(cs[cs.length-1-i],cs[cs.length-1 - (i+1)]);
			}
			
			intersectPoint(tupples);
			
			if (tupples.length == 1){
				links[0] = new double[][]{{tupples[0].getStartX(),tupples[0].getStartY()},{tupples[0].getEndX(),tupples[0].getEndY()}};
			}else{
				double[][] vs = new double[tupples.length+1][];
				
				for(int i=0;i<tupples.length;i++){
					vs[i] = new double[]{tupples[i].getStartX(),tupples[i].getStartY()};
					
				}
				vs[vs.length-1] = new double[]{tupples[tupples.length-1].getEndX(),tupples[tupples.length-1].getEndY()};
				
				links[0] = vs;
			}
		}
		
		return links;
	}
	
	//处理形状线交点
	private void intersectPoint(Tupple[] tupples){
		if (tupples.length == 1) return;

		for(int i=0;i<tupples.length-1;i++){
			
			
			Tupple start = tupples[i];
			Tupple end = tupples[i+1];
			
			if (start.isV || end.isV){
				if (start.isV && end.isV) continue;
				
				if (start.isV){
					double newY = end.getK() * start.getStartX() + end.getB();
					
					start.setEndY(newY);
					
					end.setStartX(start.getEndX());
					
					end.setStartY(newY);
					
				}else{
					double newY = start.getK() * end.getStartX() + start.getB();
					
					start.setEndX(end.getStartX());
					
					start.setEndY(newY);
					
					end.setStartY(newY);
				}
			}else{
				if (Math.abs(start.getK() - end.getK())<=0.000001) {
					continue;
					}
				
				double newX = (end.getB() - start.getB()) / (start.getK() - end.getK());
				
				double newY = newX * start.getK() + start.getB();
				
				start.setEndX(newX);
				
				start.setEndY(newY);
				
				end.setStartX(newX);
				
				end.setStartY(newY);
			}
			
		}
		
	}

	private Tupple move(double[] start, double[] end) {

		Tupple tupple = new Tupple();

		// 与X轴垂直
		if (start[0] == end[0]) {
			tupple.setV(true);

			tupple.setStartY(start[1]);

			tupple.setEndY(end[1]);

			if (end[1] > start[1]) {
				// 向右平移
				tupple.setStartX(start[0] + moveStep);

				tupple.setEndX(end[0] + moveStep);

			} else {
				// 向左平移
				tupple.setStartX(start[0] - moveStep);

				tupple.setEndX(end[0] - moveStep);
			}
		} else {
			// 不与X轴垂直
			
			//计算斜率
			tupple.setK((end[1] - start[1]) / (end[0] - start[0]));
			
			//与Y轴垂直
			if (tupple.getK() ==0){
				tupple.setStartX(start[0]);
				tupple.setEndX(end[0]);
				if (start[0] < end[0]){
					//向下平移
					tupple.setStartY(start[1] - moveStep);
					tupple.setEndY(start[1] - moveStep);
				}else{
					//向上平移
					tupple.setStartY(start[1] + moveStep);
					tupple.setEndY(start[1] + moveStep);
				}
				
				tupple.setB(tupple.getStartY());
				
			}else{
				//不与Y轴垂直
				//处理起点
				
				double vc = start[1] + (start[0] / tupple.getK());
				
				double k = -1 / tupple.getK();
				
				if (start[0] < end[0]){
					//向下平移
					if (tupple.getK()>0){
						tupple.setStartX(start[0] + (moveStep / Math.sqrt(1 + Math.pow(k, 2))));
					}else{
						tupple.setStartX(start[0] - (moveStep / Math.sqrt(1 + Math.pow(k, 2))));
					}
					
					
				}else{
					//向上平移
					
					if (tupple.getK()>0){
						tupple.setStartX(start[0] - (moveStep / Math.sqrt(1 + Math.pow(k, 2))));
					}else{
						tupple.setStartX(start[0] + (moveStep / Math.sqrt(1 + Math.pow(k, 2))));
					}
				}
				
				tupple.setStartY(-tupple.getStartX() / tupple.getK() + vc);
				
				//处理终点
				vc = end[1] + (end[0] / tupple.getK());
				
				if (start[0] < end[0]){
					//向下平移
					if (tupple.getK()>0){
						tupple.setEndX(end[0] + (moveStep / Math.sqrt(1 + Math.pow(k, 2))));
					}else{
						tupple.setEndX(end[0] - (moveStep / Math.sqrt(1 + Math.pow(k, 2))));
					}
					
					
				}else{
					//向上平移
					
					if (tupple.getK()>0){
						tupple.setEndX(end[0] - (moveStep / Math.sqrt(1 + Math.pow(k, 2))));
					}else{
						tupple.setEndX(end[0] + (moveStep / Math.sqrt(1 + Math.pow(k, 2))));
					}
				}
				
				tupple.setEndY(-tupple.getEndX() / tupple.getK() + vc);
				
				
				tupple.setB(tupple.getStartY() - (tupple.getK() * tupple.getStartX()));
				
			}
		}

		return tupple;

	}

}
