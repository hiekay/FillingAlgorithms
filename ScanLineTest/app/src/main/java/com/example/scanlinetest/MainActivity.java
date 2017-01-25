package com.example.scanlinetest;

import java.util.ArrayList;
import java.util.Stack;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;

public class MainActivity extends Activity {

	/******************************************************/
	/**
	 * ��߱��㷨
	 */
	static Point[] polygon;
	static ArrayList<Point[]> inside=null;
	static TextView squareTxt;// ���
	static TextView horizonTxt;// ���
	static TextView verticalTxt;// ���
	static TextView timeTxt;// ʱ��
	CanvasView canvasVi;
	int screenWidth;
	int screenHeight;
	static Bitmap savedBitmap;
	static Canvas savedCanvas = new Canvas();
	Paint polyPaint;

	static int NONE = -1;
	static int SEEDS = 0;
	static int SCAN = 1;
	static int POLYGON = 2;
	static int IPOLYGON = 3;
	static int CLEAN = 4;
	static int JUDGE = 5;
	static int curAlg = NONE;

	// ��ʱ
	static long enterTime;// �����㷨ʱ��
	static long exitTime;// �˳��㷨ʱ��
	static long takedTime;// �㷨��ʱ
	int num = 1;// �ظ�����

	/******************************************************/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		squareTxt = (TextView) findViewById(R.id.squareText);
		horizonTxt = (TextView) findViewById(R.id.horizonText);
		verticalTxt = (TextView) findViewById(R.id.verticalText);
		timeTxt = (TextView) findViewById(R.id.timeText);
		canvasVi = (CanvasView) findViewById(R.id.canvasView1);

		WindowManager wm = this.getWindowManager();
		screenWidth = wm.getDefaultDisplay().getWidth();
		screenHeight = wm.getDefaultDisplay().getHeight();

		savedBitmap = Bitmap.createBitmap(screenWidth, screenHeight,
				Config.ARGB_8888);
		savedCanvas.setBitmap(savedBitmap);

		polyPaint = new Paint();
		polyPaint.setColor(Color.RED);
		polyPaint.setStrokeWidth(1);

		inside=new ArrayList<Point[]>();
	}

	// ��ͳ��߱����(��ɨ)
	public void onHorizontalPolygonFillBtn(View v) {
		curAlg = POLYGON;
		calDxDy();
		
		enterTime = System.currentTimeMillis();
		/**************************************************/
		for (int n = 1; n <= num; n++) {
			NETY net = new NETY();
			net.getMinMax();
			net.create();

			AETY aet = new AETY();
			for (int i = 0; i < net.ymax - net.ymin + 1; i++) {
				// NET ת�� AET
				aet.insertNETY(net, i);
				aet.updateAETY(net, i);
				aet.Fill(net, i);
			}
		}
		/**************************************************/
		exitTime = System.currentTimeMillis();
		takedTime = exitTime - enterTime;
		timeTxt.setText(Double.toString(takedTime * 1.0 / num));

		canvasVi.invalidate();// ˢ�»���
	}

	// ��ͳ��߱����(��ɨ)
	public void onVerticalPolygonFillBtn(View v) {
		curAlg = POLYGON;
		calDxDy();
		
		enterTime = System.currentTimeMillis();
		/**************************************************/
		for (int n = 1; n <= num; n++) {
			NETX net = new NETX();
			net.getMinMax();
			net.create();

			AETX aet = new AETX();
			for (int i = 0; i < net.xmax - net.xmin + 1; i++) {
				// NET ת�� AET
				aet.insertNETX(net, i);
				aet.updateAETX(net, i);
				aet.Fill(net, i);
			}
		}
		/**************************************************/
		exitTime = System.currentTimeMillis();
		takedTime = exitTime - enterTime;
		timeTxt.setText(Double.toString(takedTime * 1.0 / num));

		canvasVi.invalidate();// ˢ�»���
	}

	// �Ľ���߱����
	public void onImprovedPolygonFillBtn(View v) {
		curAlg = IPOLYGON;
		calDxDy();
		
		enterTime = System.currentTimeMillis();
		/**************************************************/
		for (int n = 1; n <= num; n++) {

			// Ѱ���ݶȺͺ����ֵ
			int Horizon = 0, Vertical = 0;
			int circle = polygon.length;
			int rear;
			for (int i = 0; i < circle; i++) {
				rear = (i + 1) % circle;
				Horizon += Math.abs(polygon[rear].x - polygon[i].x);
				Vertical += Math.abs(polygon[rear].y - polygon[i].y);
			}

			if (Vertical <= Horizon) // �����ͣ���ɨ
			{
				NETY net = new NETY();
				net.getMinMax();
				net.create();

				AETY aet = new AETY();
				for (int i = 0; i < net.ymax - net.ymin + 1; i++) {
					// NET ת�� AET
					aet.insertNETY(net, i);
					aet.updateAETY(net, i);
					aet.Fill(net, i);
				}
			} else // �����ͣ���ɨ
			{
				NETX net = new NETX();
				net.getMinMax();
				net.create();

				AETX aet = new AETX();
				for (int i = 0; i < net.xmax - net.xmin + 1; i++) {
					// NET ת�� AET
					aet.insertNETX(net, i);
					aet.updateAETX(net, i);
					aet.Fill(net, i);
				}
			}
		}
		/**************************************************/
		exitTime = System.currentTimeMillis();
		takedTime = exitTime - enterTime;
		timeTxt.setText(Double.toString(takedTime * 1.0 / num));

		canvasVi.invalidate();// ˢ�»���
	}
public void onBtn(View v)
{
	curAlg = 456;
	
	NETY net = new NETY();
	net.getMinMax();
	net.create();

	AETY aet = new AETY();
	for (int i = 0; i < net.ymax - net.ymin + 1; i++) {
		// NET ת�� AET
		aet.insertNETY(net, i);
		aet.updateAETY(net, i);
		aet.Fill(net, i);
	}

	canvasVi.invalidate();// ˢ�»���
}
	// ���ӵݹ����
	public void onSeedsFillBtn(View v) {
		curAlg = SEEDS;
	}

	// ɨ�������
	public void onScanlineFillBtn(View v) {
		curAlg = SCAN;
	}

	// ������
	public void onJudgeFillBtn(View v) {
		curAlg = JUDGE;
		judgeFill();
	}

	// ���
	public void onCleanBtn(View v) {
		curAlg = CLEAN;
		Paint paint = new Paint();
		paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		savedCanvas.drawPaint(paint);

		savedCanvas.drawPath(CanvasView.path, CanvasView.drawPaint);
		canvasVi.invalidate();
	}

	/***************************************************************************/
	/**
	 * ��ͳ��߱��㷨
	 * 
	 * @author chicken
	 * 
	 */
	// �߽��(AET\NET)
	class EdgeY {
		double xi;
		double dx;
		int ym;
		EdgeY next = null;

		EdgeY(double xi, double dx, int ym) {
			this.xi = xi;
			this.dx = dx;
			this.ym = ym;
		}
	}

	/**
	 * NET��
	 */
	class NETY {
		EdgeY[] bucket;// Ͱͷ���(ɨ��������)
		int ymin, ymax;

		void create() {
			createNETY();// ����NET

		}

		void getMinMax() {
			ymin = polygon[0].y;
			ymax = polygon[0].y;
			for (int i = 1; i < polygon.length; i++) {
				if (polygon[i].y > ymax) {
					ymax = polygon[i].y;
				}
				if (polygon[i].y < ymin) {
					ymin = polygon[i].y;
				}
			}
		}

		void createNETY() {
			// ����ɨ����������Ͱ���
			bucket = new EdgeY[ymax - ymin + 1];

			int n = polygon.length;
			for (int y = ymin; y <= ymax; y++) {
				// ����Ͱͷ��㲢��ʼ��
				bucket[y - ymin] = new EdgeY(0, 0, y);
				EdgeY tmp = bucket[y - ymin];// �������α�

				// �Ƿ��붥���ཻ
				for (int cur = 0; cur < n; cur++) {
					// ɨ���ߴ�Խ����
					if (y == polygon[cur].y) {

						int front = (cur - 1 + n) % n;// ǰ��
						int rear = (cur + 1) % n;// ���
						EdgeY firstEdge = null, secondEdge = null;
						double firstDx = -9999;
						double secondDx = -9999;

						// ǰ�������ڸߴ�ʱ����
						if (polygon[front].y > y) {
							// ����߽�㲢��ʼ��
							firstDx = (polygon[cur].x - polygon[front].x) * 1.0
									/ (polygon[cur].y - polygon[front].y);
							firstEdge = new EdgeY(polygon[cur].x, firstDx,
									polygon[front].y);
							tmp.next = firstEdge;
							tmp = firstEdge;
						}

						// ��̶����ڸߴ�ʱ����
						if (polygon[rear].y > y) {
							// ����߽�㲢��ʼ��
							secondDx = (polygon[cur].x - polygon[rear].x) * 1.0
									/ (polygon[cur].y - polygon[rear].y);
							secondEdge = new EdgeY(polygon[cur].x, secondDx,
									polygon[rear].y);
							tmp.next = secondEdge;
							tmp = secondEdge;
						}
					}
				}
				/**************************************/
				if(inside.size() != 0) //û�п׶������
				{
					//�ڵ���NET
					for(int i=0;i<inside.size();i++)
					{	
						Point[] in=inside.get(i);
			
						// �Ƿ��붥���ཻ
						int m = in.length;
						for (int cur = 0; cur < m; cur++) {
							// ɨ���ߴ�Խ����
							if (y == in[cur].y) {
	
								int front = (cur - 1 + m) % m;// ǰ��
								int rear = (cur + 1) % m;// ���
								EdgeY firstEdge = null, secondEdge = null;
								double firstDx = -9999;
								double secondDx = -9999;
	
								// ǰ�������ڸߴ�ʱ����
								if (in[front].y > y) {
									// ����߽�㲢��ʼ��
									firstDx = (in[cur].x - in[front].x) * 1.0
											/ (in[cur].y - in[front].y);
									firstEdge = new EdgeY(in[cur].x, firstDx,
											in[front].y);
									tmp.next = firstEdge;
									tmp = firstEdge;
								}
	
								// ��̶����ڸߴ�ʱ����
								if (in[rear].y > y) {
									// ����߽�㲢��ʼ��
									secondDx = (in[cur].x - in[rear].x) * 1.0
											/ (in[cur].y - in[rear].y);
									secondEdge = new EdgeY(in[cur].x, secondDx,
											in[rear].y);
									tmp.next = secondEdge;
									tmp = secondEdge;
								}
							}
						}
					}
				}
				/**************************************/
			}		
		}

		// ����NET
		void Traverse() {
			for (int i = 0; i < bucket.length; i++) {
				EdgeY tmp = bucket[i];
				System.out.println("ɨ���ߣ�" + tmp.ym);
				while (tmp.next != null) {
					System.out.println(tmp.next.dx + "");
					tmp = tmp.next;
				}
			}
		}
	}

	/**
	 * AET��
	 */
	class AETY {
		EdgeY head;

		AETY() {
			head = new EdgeY(0, 0, 0);
			head.next = null;
		}

		// ����line��ɨ���ߵ������±�����������߱�
		void insertNETY(NETY net, int line) {
			EdgeY curNET = net.bucket[line].next;// �ҵ�Ͱ���
			EdgeY rearNET;
			EdgeY curAET = head;// �ҵ�ͷ���
			EdgeY rearAET;
			while (curNET != null)// NET��δ��
			{
				rearNET = curNET.next; // ��ǰ׼���ú�̽�㣬�Ա���ǰ��㱻���Ժ���Լ�������NET
				while (curAET != null) { // AET��δ��
					// �бƵĻ��ͽ������
					if (curNET.xi >= curAET.xi
							&& (curAET.next == null || curNET.xi <= curAET.next.xi)) {
						rearAET = curAET.next;
						curAET.next = curNET;
						curNET.next = rearAET;

						curAET = head; // ָ�뻹ԭ
						break;
					} else {
						curAET = curAET.next;
					}
				}
				curNET = rearNET;
			}
		}

		// ɾ������ym�ı߲�����xi����ֵ
		void updateAETY(NETY net, int line) {

			// ɾ���ɱ�
			EdgeY prior = head;// �ҵ�ͷ���
			EdgeY cur = head.next;
			while (cur != null) {
				if (net.bucket[line].ym == cur.ym) // Խ��ĳ�߾�ɾ���ñ�
				{
					prior.next = cur.next;
					cur = prior.next;
				} else {
					if (prior.next == null)
						break;
					else {
						prior = prior.next;
						cur = prior.next;
					}
				}
			}

			// �������Ը���AET�б�
			EdgeY tmp = head.next;// �ҵ�ͷ���
			while (tmp != null) {
				tmp.xi += tmp.dx;
				tmp = tmp.next;
			}

			if (curAlg == POLYGON) // ��ͳ��߱�
			{
				// ð��������
				EdgeY begin = head; // ��ǰ�˿�ʼ����ǰ�����
				EdgeY walk; // ���߽��
				while (begin.next != null) {
					prior = begin;
					walk = begin.next;
					while (walk.next != null) {
						EdgeY rear = walk.next;
						if (walk.xi > rear.xi) {
							prior.next = rear;
							walk.next = rear.next;
							rear.next = walk;
						}

						prior = prior.next;
						walk = prior.next;
					}
					begin = begin.next;
				}
			} else if(curAlg == IPOLYGON)// �Ľ���߱����ж���û���������ߵ�xi��ͬ��
			{
				EdgeY front = head;
				EdgeY walk = head.next;// �ҵ�ͷ���
				EdgeY behind;

				if (head.next != null) {
					while (walk.next != null) {
						behind = walk.next;
						if (walk.xi > behind.xi) {
							front.next = behind;
							walk.next = behind.next;
							behind.next = walk;
						}
						front = front.next;
						walk = front.next;
					}
				}
			}
		}

		// �����������
		void Fill(NETY net, int line) {
			// �������Ը���AET�б�
			EdgeY first = head.next;// �ҵ�ͷ���

			if (first != null) // �Ѿ�û�н����˾��˳�
			{
				EdgeY second = first.next;
				while (second != null) {
					savedCanvas.drawLine((float) first.xi,
							(float) net.bucket[line].ym, (float) second.xi,
							(float) net.bucket[line].ym, polyPaint);

					if (second.next == null)
						break;
					else {
						first = first.next.next;
						second = second.next.next;
					}
				}
			}
		}

		// ����AET
		void Traverse() {
			EdgeY tmp = head.next;// �ҵ�ͷ���

			while (tmp != null) {
				System.out.println(tmp.xi);
				tmp = tmp.next;
			}
			System.out.println("��");
		}
	}

	/***************************************************************************/
	/***************************************************************************/
	// ɨ����ΪX

	// �߽��(AET\NET)
	class EdgeX {
		double yi;
		double dy;
		int xm;
		EdgeX next = null;

		EdgeX(double yi, double dy, int xm) {
			this.yi = yi;
			this.dy = dy;
			this.xm = xm;
		}
	}

	/**
	 * NET��
	 */
	class NETX {
		EdgeX[] bucket;// Ͱͷ���(ɨ��������)
		int xmin, xmax;

		void create() {
			createNETX();// ����NET

		}

		void getMinMax() {
			xmin = polygon[0].x;
			xmax = polygon[0].x;
			for (int i = 1; i < polygon.length; i++) {
				if (polygon[i].x > xmax) {
					xmax = polygon[i].x;
				}
				if (polygon[i].x < xmin) {
					xmin = polygon[i].x;
				}
			}
		}

		void createNETX() {
			// ����ɨ����������Ͱ���
			bucket = new EdgeX[xmax - xmin + 1];

			int n = polygon.length;
			for (int x = xmin; x <= xmax; x++) {
				// ����Ͱͷ��㲢��ʼ��
				bucket[x - xmin] = new EdgeX(0, 0, x);
				EdgeX tmp = bucket[x - xmin];// �������α�

				// �Ƿ��붥���ཻ
				for (int cur = 0; cur < n; cur++) {
					// ɨ���ߴ�Խ����
					if (x == polygon[cur].x) {

						int front = (cur - 1 + n) % n;// ǰ��
						int rear = (cur + 1) % n;// ���
						EdgeX firstEdge = null, secondEdge = null;
						double firstDy = -9999;
						double secondDy = -9999;

						// ǰ�������ڸߴ�ʱ����
						if (polygon[front].x > x) {
							// ����߽�㲢��ʼ��
							firstDy = (polygon[cur].y - polygon[front].y) * 1.0
									/ (polygon[cur].x - polygon[front].x);
							firstEdge = new EdgeX(polygon[cur].y, firstDy,
									polygon[front].x);
							tmp.next = firstEdge;
							tmp = firstEdge;
						}

						// ��̶����ڸߴ�ʱ����
						if (polygon[rear].x > x) {
							// ����߽�㲢��ʼ��
							secondDy = (polygon[cur].y - polygon[rear].y) * 1.0
									/ (polygon[cur].x - polygon[rear].x);
							secondEdge = new EdgeX(polygon[cur].y, secondDy,
									polygon[rear].x);
							tmp.next = secondEdge;
							tmp = secondEdge;
						}
					}
				}
				/**************************************/
				if(inside.size() != 0) //û�п׶������
				{
					//�ڵ���NET
					for(int i=0;i<inside.size();i++)
					{	
						Point[] in=inside.get(i);
			
						// �Ƿ��붥���ཻ
						int m = in.length;
						for (int cur = 0; cur < m; cur++) {
							// ɨ���ߴ�Խ����
							if (x == in[cur].x) {
	
								int front = (cur - 1 + m) % m;// ǰ��
								int rear = (cur + 1) % m;// ���
								EdgeX firstEdge = null, secondEdge = null;
								double firstDy = -9999;
								double secondDy = -9999;
	
								// ǰ�������ڸߴ�ʱ����
								if (in[front].x > x) {
									// ����߽�㲢��ʼ��
									firstDy = (in[cur].y - in[front].y) * 1.0
											/ (in[cur].x - in[front].x);
									firstEdge = new EdgeX(in[cur].y, firstDy,
											in[front].x);
									tmp.next = firstEdge;
									tmp = firstEdge;
								}
	
								// ��̶����ڸߴ�ʱ����
								if (in[rear].x > x) {
									// ����߽�㲢��ʼ��
									secondDy = (in[cur].y - in[rear].y) * 1.0
											/ (in[cur].x - in[rear].x);
									secondEdge = new EdgeX(in[cur].y, secondDy,
											in[rear].x);
									tmp.next = secondEdge;
									tmp = secondEdge;
								}
							}
						}
					}
				}
				/**************************************/
			}	
		}

		// ����NET
		void Traverse() {
			for (int i = 0; i < bucket.length; i++) {
				EdgeX tmp = bucket[i];
				System.out.println("ɨ���ߣ�" + tmp.xm);
				while (tmp.next != null) {
					System.out.println(tmp.next.dy + "");
					tmp = tmp.next;
				}
			}
		}
	}

	/**
	 * AET��
	 */
	class AETX {
		EdgeX head;

		AETX() {
			head = new EdgeX(0, 0, 0);
			head.next = null;
		}

		// ����line��ɨ���ߵ������±�����������߱�
		void insertNETX(NETX net, int line) {
			EdgeX curNET = net.bucket[line].next;// �ҵ�Ͱ���
			EdgeX rearNET;
			EdgeX curAET = head;// �ҵ�ͷ���
			EdgeX rearAET;
			while (curNET != null)// NET��δ��
			{
				rearNET = curNET.next; // ��ǰ׼���ú�̽�㣬�Ա���ǰ��㱻���Ժ���Լ�������NET
				while (curAET != null) { // AET��δ��
					// �бƵĻ��ͽ������
					if (curNET.yi >= curAET.yi
							&& (curAET.next == null || curNET.yi <= curAET.next.yi)) {
						rearAET = curAET.next;
						curAET.next = curNET;
						curNET.next = rearAET;

						curAET = head; // ָ�뻹ԭ
						break;
					} else {
						curAET = curAET.next;
					}
				}
				curNET = rearNET;
			}
		}

		// ɾ������ym�ı߲�����xi����ֵ
		void updateAETX(NETX net, int line) {

			// ɾ���ɱ�
			EdgeX prior = head;// �ҵ�ͷ���
			EdgeX cur = head.next;
			while (cur != null) {
				if (net.bucket[line].xm == cur.xm) // Խ��ĳ�߾�ɾ���ñ�
				{
					prior.next = cur.next;
					cur = prior.next;
				} else {
					if (prior.next == null)
						break;
					else {
						prior = prior.next;
						cur = prior.next;
					}
				}
			}

			// �������Ը���AET�б�
			EdgeX tmp = head.next;// �ҵ�ͷ���
			while (tmp != null) {
				tmp.yi += tmp.dy;
				tmp = tmp.next;
			}

			if (curAlg == POLYGON) // ��ͳ��߱�
			{
				// ð��������
				EdgeX begin = head; // ��ǰ�˿�ʼ����ǰ�����
				EdgeX walk; // ���߽��
				while (begin.next != null) {
					prior = begin;
					walk = begin.next;
					while (walk.next != null) {
						EdgeX rear = walk.next;
						if (walk.yi > rear.yi) {
							prior.next = rear;
							walk.next = rear.next;
							rear.next = walk;
						}

						prior = prior.next;
						walk = prior.next;
					}
					begin = begin.next;
				}
			} else if(curAlg == IPOLYGON)// �Ľ���߱����ж���û���������ߵ�xi��ͬ��
			{
				EdgeX front = head;
				EdgeX walk = head.next;// �ҵ�ͷ���
				EdgeX behind;

				if (head.next != null) {
					while (walk.next != null) {
						behind = walk.next;
						if (walk.yi > behind.yi) {
							front.next = behind;
							walk.next = behind.next;
							behind.next = walk;
						}
						front = front.next;
						walk = front.next;
					}
				}
			}
		}

		// �����������
		void Fill(NETX net, int line) {
			// �������Ը���AET�б�
			EdgeX first = head.next;// �ҵ�ͷ���

			if (first != null) // �Ѿ�û�н����˾��˳�
			{
				EdgeX second = first.next;
				while (second != null) {
					savedCanvas.drawLine((float) net.bucket[line].xm,
							(float) first.yi, (float) net.bucket[line].xm,
							(float) second.yi, polyPaint);

					if (second.next == null)
						break;
					else {
						first = first.next.next;
						second = second.next.next;
					}
				}
			}
		}

		// ����AET
		void Traverse() {
			EdgeX tmp = head.next;// �ҵ�ͷ���

			while (tmp != null) {
				System.out.println(tmp.yi);
				tmp = tmp.next;
			}
			System.out.println("��");
		}
	}

	/***************************************************************************/
	/**
	 * ����б��㷨
	 * 
	 * @author chicken
	 * 
	 */
	public void judgeFill() // ����б�����ڵ�
	{
		int width = savedBitmap.getWidth();
		int height = savedBitmap.getHeight();
		int fillColor = Color.RED;

		enterTime = System.currentTimeMillis();
		/**************************************************/
		for (int x = 0; x <= width; x++) {
			for (int y = 0; y <= height; y++) {
				if (isInside(new Point(x, y))) {
					savedBitmap.setPixel(x, y, fillColor);
				}
			}
		}
		/**************************************************/
		exitTime = System.currentTimeMillis();
		takedTime = exitTime - enterTime;
		timeTxt.setText(Long.toString(takedTime));

		canvasVi.invalidate();
	}

	public boolean isInside(Point pt) {
		int i, j;
		boolean inside = false, redo = false;
		int N = polygon.length;
		redo = true;
		for (i = 0; i < N; ++i) {
			if (polygon[i].x == pt.x && // �Ƿ��ڶ�����
					polygon[i].y == pt.y) {
				redo = false;
				inside = true;
				break;
			}
		}

		while (redo) {
			redo = false;
			inside = false;
			for (i = 0, j = N - 1; i < N; j = i++) {
				if ((polygon[i].y < pt.y && pt.y < polygon[j].y)
						|| (polygon[j].y < pt.y && pt.y < polygon[i].y)) {
					if (pt.x <= polygon[i].x || pt.x <= polygon[j].x) {
						double _x = (pt.y - polygon[i].y)
								* (polygon[j].x - polygon[i].x)
								/ (polygon[j].y - polygon[i].y) + polygon[i].x;
						if (pt.x < _x) // ���ߵ����
							inside = !inside;
						else if (pt.x == _x) // ������
						{
							inside = true;
							break;
						}
					}
				} else if (pt.y == polygon[i].y) {
					if (pt.x < polygon[i].x) // �����ڶ�����
					{
						if (polygon[i].y > polygon[j].y)
							--pt.y;
						else
							++pt.y;
						redo = true;
						break;
					}
				} else if (polygon[i].y == polygon[j].y
						&& // ��ˮƽ�ı߽�����
						pt.y == polygon[i].y
						&& ((polygon[i].x < pt.x && pt.x < polygon[j].x) || (polygon[j].x < pt.x && pt.x < polygon[i].x))) {
					inside = true;
					break;
				}
			}
		}

		return inside;
	}

	/***************************************************************************/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			android.os.Process.killProcess(android.os.Process.myPid());// ɱ������
			MainActivity.this.onDestroy();// �ݻٻ
			System.exit(0);// ����ϵͳ
		}
		return super.onKeyDown(keyCode, event);
	}

	public void calDxDy()
	{
		//Ѱ���ݶȺͺ����ֵ
		int dx=0,dy=0;
		int circle=MainActivity.polygon.length;
		int rear;
		
		//outside
		for(int i=0;i<circle;i++)
		{
			rear=(i+1)%circle;
			dx+=Math.abs(MainActivity.polygon[rear].x-MainActivity.polygon[i].x);
			dy+=Math.abs(MainActivity.polygon[rear].y-MainActivity.polygon[i].y);
		}
		
		//inside
		for(int i=0;i<inside.size();i++)
		{	
			Point[] in=inside.get(i);
			
			circle=in.length;
			for(int j=0;j<circle;j++)
			{
				rear=(j+1)%circle;
				dx+=Math.abs(in[rear].x-in[j].x);
				dy+=Math.abs(in[rear].y-in[j].y);
			}
		}
		
		
		//����ת��������
		float Dx,Dy;
		Dx=(float)((dx*1.0/480)*10.16);
		Dy=(float)((dy*1.0/854)*18.08);
		//��ʾ
		horizonTxt.setText(Float.toString(Dx));
		verticalTxt.setText(Float.toString(Dy));
	}
}
