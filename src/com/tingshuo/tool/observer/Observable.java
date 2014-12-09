package com.tingshuo.tool.observer;

import java.util.Vector;

public class Observable {
	private boolean changed = false;
	private Vector<Observer> obs;

	/** Construct an Observable with zero Observers. */

	public Observable() {
		obs = new Vector<Observer>();
	}

	/**
	 * ��һ���۲�����ӵ��۲��߾ۼ�����
	 */
	public synchronized void addObserver(Observer o) {
		if (o == null)
			throw new NullPointerException();
		if (!obs.contains(o)) {
			obs.addElement(o);
		}
	}

	/**
	 * ��һ���۲��ߴӹ۲��߾ۼ���ɾ��
	 */
	public synchronized void deleteObserver(Observer o) {
		obs.removeElement(o);
	}

	public void notifyObservers() {
		notifyObservers(null);
	}

	/**
	 * ����������б仯����ʱhasChanged �����᷵��true�� ���ñ�����֪ͨ���еǼǵĹ۲��ߣ����������ǵ�update()����
	 * ����this��arg��Ϊ����
	 */
	public void notifyObservers(Object arg) {

		Object[] arrLocal;

		synchronized (this) {

			if (!changed)
				return;
			arrLocal = obs.toArray();
			clearChanged();
		}

		for (int i = arrLocal.length - 1; i >= 0; i--)
			((Observer) arrLocal[i]).update(this, arg);
	}

	/**
	 * ���۲��߾ۼ����
	 */
	public synchronized void deleteObservers() {
		obs.removeAllElements();
	}

	/**
	 * �����ѱ仯������Ϊtrue
	 */
	public synchronized void setChanged() {
		changed = true;
	}

	/**
	 * �����ѱ仯������Ϊfalse
	 */
	public synchronized void clearChanged() {
		changed = false;
	}

	/**
	 * ��Ȿ�����Ƿ��ѱ仯
	 */
	public synchronized boolean hasChanged() {
		return changed;
	}

	/**
	 * Returns the number of observers of this <tt>Observable</tt> object.
	 * 
	 * @return the number of observers of this object.
	 */
	public synchronized int countObservers() {
		return obs.size();
	}
}