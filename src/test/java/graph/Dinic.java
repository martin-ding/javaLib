package graph;

        import java.util.ArrayList;
        import java.util.Collections;
        import java.util.HashMap;
        import java.util.Iterator;
        import java.util.LinkedList;
        import java.util.Queue;

public class Dinic {
    //定义一个哈希表，即字典，构造原始的图
    HashMap<String, HashMap<String,Integer>> original_graph;
    HashMap<String, HashMap<String,Integer>> residual_graph;
    //定义层次图，层次图的边只能从起点到1，1到2，2到3，以此类推
    HashMap<String, HashMap<String,Integer>> level_graph;
    ArrayList<Integer> residual_flow_value = new ArrayList<Integer>();
    ArrayList<String> residual_flow_path = new ArrayList<String>();
    int augmented_value, maximum_flow;
    boolean is_blocking_flow = false;
    //流量 flow 容量  Capacity

    public Dinic() {
        // TODO Auto-generated constructor stub
        original_graph = new HashMap<String, HashMap<String,Integer>>();
        residual_graph = new HashMap<String, HashMap<String,Integer>>();
        level_graph = new HashMap<String, HashMap<String,Integer>>();
        augmented_value = 0;
    }

    public void initial_original_graph() {//只能用来查询，不能修改
//    	Integer[] weight_edge = new Integer[]{1,2};
        original_graph.put("s",new HashMap<String,Integer>());//只出不进
        //1
//    	original_graph.get("s").put("v1",10);
//    	original_graph.get("s").put("v2",10);
////    	System.out.println(original_graph.get("s"));
//    	original_graph.put("v1",new HashMap<String,Integer>());
//    	original_graph.get("v1").put("v2",2);
//    	original_graph.get("v1").put("v3",4);
//    	original_graph.get("v1").put("v4",8);
//    	original_graph.put("v2",new HashMap<String,Integer>());
//    	original_graph.get("v2").put("v4",9);
//    	original_graph.put("v3",new HashMap<String,Integer>());
//    	original_graph.get("v3").put("t",10);
//    	original_graph.put("v4",new HashMap<String,Integer>());
//    	original_graph.get("v4").put("v3",6);
//    	original_graph.get("v4").put("t",10);
//    	original_graph.put("t",new HashMap<String,Integer>());//只进不出
        //2
        original_graph.get("s").put("v1",8);original_graph.get("s").put("v2",12);
        original_graph.put("v1",new HashMap<String,Integer>());
        original_graph.get("v1").put("v3",6);original_graph.get("v1").put("v4",10);
        original_graph.put("v2",new HashMap<String,Integer>());original_graph.get("v2").put("v1",2);
        original_graph.get("v2").put("v3",10);original_graph.put("v3",new HashMap<String,Integer>());
        original_graph.get("v3").put("t",8);original_graph.put("v4",new HashMap<String,Integer>());
        original_graph.get("v4").put("v3",2);original_graph.get("v4").put("t",10);
        original_graph.put("t",new HashMap<String,Integer>());
//    	System.out.println(original_graph.get("t").isEmpty());
    }

    public void initial_residual_graph() { // 初始化构造方法和original_graph一样
        //1
//    	residual_graph.put("s",new HashMap<String,Integer>());residual_graph.get("s").put("v1",10);
//    	residual_graph.get("s").put("v2",10);residual_graph.put("v1",new HashMap<String,Integer>());
//    	residual_graph.get("v1").put("v2",2);residual_graph.get("v1").put("v3",4);
//    	residual_graph.get("v1").put("v4",8);residual_graph.put("v2",new HashMap<String,Integer>());
//    	residual_graph.get("v2").put("v4",9);residual_graph.put("v3",new HashMap<String,Integer>());
//    	residual_graph.get("v3").put("t",10);residual_graph.put("v4",new HashMap<String,Integer>());
//    	residual_graph.get("v4").put("v3",6);residual_graph.get("v4").put("t",10);
//    	residual_graph.put("t",new HashMap<String,Integer>());
        //2
        residual_graph.put("s",new HashMap<String,Integer>());residual_graph.get("s").put("v1",8);
        residual_graph.get("s").put("v2",12);residual_graph.put("v1",new HashMap<String,Integer>());
        residual_graph.get("v1").put("v3",6);residual_graph.get("v1").put("v4",10);
        residual_graph.put("v2",new HashMap<String,Integer>());residual_graph.get("v2").put("v1",2);
        residual_graph.get("v2").put("v3",10);residual_graph.put("v3",new HashMap<String,Integer>());
        residual_graph.get("v3").put("t",8);residual_graph.put("v4",new HashMap<String,Integer>());
        residual_graph.get("v4").put("v3",2);residual_graph.get("v4").put("t",10);
        residual_graph.put("t",new HashMap<String,Integer>());
    }

    //使用bfs
    public void initial_level_graph() {
        //初始化变量
        level_graph = new HashMap<String, HashMap<String,Integer>>();
        Queue<String> queue = new LinkedList<String>();
        ArrayList<String> traversed_node = new ArrayList<String>();//遍历过的点
        HashMap<String,Integer> node_level = new HashMap<String,Integer>();
        node_level.put("s",0);
        traversed_node.add("s");
        String pointer;
        queue.offer("s");
        while(!queue.isEmpty()) {
            pointer = queue.poll();
            Iterator<String> iterator = residual_graph.get(pointer).keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();//表示当前点pointer有连接点，但是不确定这个点是否能加入层次图
                if(!traversed_node.contains(key)) {
                    if (residual_graph.get(pointer).get(key) > 0 ) { // 表示能通过
                        node_level.put(key,node_level.get(pointer)+1);
                        traversed_node.add(key);// 添加遍历过的节点
                        queue.offer(key); // 这一步会把同一level的点全部加到队列中去
                    }
                }
                //下面这个判断条件可以优化
                if (node_level.get(key) != null && node_level.get(pointer) != null) {
                    if (node_level.get(key) > node_level.get(pointer)) {
                        int value = residual_graph.get(pointer).get(key); //边的剩余容量
                        // 只有容量大于0才能通过
                        if (value > 0) {
                            if (!level_graph.containsKey(pointer)){ // 没有这个key就要重新初始化
                                level_graph.put(pointer,new HashMap<String,Integer>());
                            }
                            level_graph.get(pointer).put(key,value);
                        }
//                      System.out.println(level_graph);
                    }
                }
            }
        }
    }
    //dfs，回溯
    //寻找层次图的阻塞流
    public void find_blocking_flow(String node) {
        HashMap<String,Integer> tempHashMap = level_graph.get(node);
//    	System.out.println(tempHashMap);
        if (node == "t") {
            is_blocking_flow = true;
            ArrayList<Integer> tempArray = new ArrayList<>(residual_flow_value);
            Collections.sort(tempArray);
            augmented_value = tempArray.get(0);
            for (int id = 0; id < residual_flow_path.size()-1 ;id++) {
                level_graph.get(residual_flow_path.get(id)).replace(residual_flow_path.get(id+1),
                        level_graph.get(residual_flow_path.get(id)).get(residual_flow_path.get(id+1))-augmented_value);
            }
            for (int id = 0; id < residual_flow_value.size() ;id++) { // 存值数组需要动态更新
                residual_flow_value.set(id, residual_flow_value.get(id)-augmented_value);
            }
//    		System.out.println(level_graph);
//    		System.out.println(residual_flow_path); //从起点到终点的所有通路
        }
        if (tempHashMap == null)  {
            // 根据层次图的处理，决定使用tempHashMap.isEmpty()还是tempHashMap == null
            return;
        }
        Iterator<String> iterator = tempHashMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            int value = tempHashMap.get(key);
            if (value > 0) {
                residual_flow_value.add(value);
                residual_flow_path.add(key);
//            	System.out.println(residual_flow_value);
                find_blocking_flow(key);
//              System.out.println(key);
                residual_flow_value.remove(residual_flow_value.size()-1);//去掉最后一个
                residual_flow_path.remove(residual_flow_path.size()-1);//去掉最后一个
            }
        }
    }
    public void updateResidualGraph() {
        // 在层次图寻找到一条阻塞流之后，更新残留网络的流量
        Iterator<String> iterator_1 = level_graph.keySet().iterator();
        while (iterator_1.hasNext()) {
            String key_1 = iterator_1.next();
            HashMap<String,Integer> tempHashMap = level_graph.get(key_1);
            Iterator<String> iterator_2 = tempHashMap.keySet().iterator();
            while(iterator_2.hasNext()) {
                String key_2 = iterator_2.next();
                int value = tempHashMap.get(key_2);
                residual_graph.get(key_1).replace(key_2, value);
            }
        }
        // 给残留图添加反向边
        int inversed_value = 0;
        Iterator<String> iterator_3 = level_graph.keySet().iterator();
        while(iterator_3.hasNext()) {
            String key_3 = iterator_3.next();
            // 数组迭代器不能在迭代过程中改变，所以需要有一个不变的数据结构
            HashMap<String,Integer> tempHashMap = level_graph.get(key_3);
            Iterator<String> iterator_4 = tempHashMap.keySet().iterator();
            while(iterator_4.hasNext()) {
                String key_4 = iterator_4.next();
                // original_graph.get(key_3).get(key_4) 和 residual_graph.get(key_3).get(key_4) 一定不是null，
                // 考虑到原始图没有反向边
                if (original_graph.get(key_3).get(key_4) != null) {
                    inversed_value = original_graph.get(key_3).get(key_4)- residual_graph.get(key_3).get(key_4);
                }else {
                    inversed_value = original_graph.get(key_4).get(key_3)- residual_graph.get(key_3).get(key_4);
                }
                residual_graph.get(key_4).put(key_3,inversed_value);
            }
            inversed_value = 0;
        }
        // 在dinic算法中，我一开始认为没有必要构造从汇点到汇点上一层的点、源点下一层到源点的反向边了。但我后来觉得不应该。
        // 切记，更新的是残留图，不是层次图。
    }
    public void dinic_max_flow() {
        // 初始化原始的网络图
        initial_original_graph();
        // 初始化残留网络，等于原始的网络图
        initial_residual_graph();
        System.out.println("原始网络图: "+original_graph);
        int iteration = 0;
        while(true) {
            // 初始化残留网络的层次图
            iteration++;
            System.out.println("---------第"+iteration+"次迭代"+"---------");
            initial_level_graph();
            System.out.println("层次图: "+level_graph);
            residual_flow_path.clear();
            residual_flow_path.add("s");
            // 从源点开始寻找改层次图的阻塞流，多个增广路构成阻塞流
            find_blocking_flow("s");
            System.out.println("增广后的层次图: "+level_graph);
            if (!is_blocking_flow) {
                // 一旦残留网络没办法找到阻塞流了，中止循环
                break;
            }
            is_blocking_flow = false;
            //找到之后，开始更新残留网络，并添加反向边
            updateResidualGraph();
            System.out.println("更新流量后的残留图: "+residual_graph);
        }
        System.out.println("最终的残留图: "+residual_graph);
        System.out.println("---------最大流---------");
        get_max_flow();
    }

    public void get_max_flow() {
        int capacity = 0;
        /*计算最大容量不能计算原始图源点的出度，注释掉的是错误的*/
//		HashMap<String,Integer> capacityHashMap = original_graph.get("s");
//		Iterator<String> iterator_1 = capacityHashMap.keySet().iterator();
//		while(iterator_1.hasNext()) {
//			String key_1 = iterator_1.next();
//			capacity = capacity + capacityHashMap.get(key_1);
//		}
        Iterator<String> iterator_1 = original_graph.keySet().iterator();
        while(iterator_1.hasNext()) {
            String key_1 = iterator_1.next();
            HashMap<String,Integer> flowHashMap = original_graph.get(key_1);
            Iterator<String> iterator_2 = flowHashMap.keySet().iterator();
            while(iterator_2.hasNext()) {
                String key_2 = iterator_2.next();
                if (key_2 == "t") {
                    capacity = capacity + flowHashMap.get(key_2);
                }
            }
        }
        int residual = 0;
        Iterator<String> iterator_3 = residual_graph.keySet().iterator();
        while(iterator_3.hasNext()) {
            String key_3 = iterator_3.next();
            HashMap<String,Integer> flowHashMap = residual_graph.get(key_3);
            Iterator<String> iterator_4 = flowHashMap.keySet().iterator();
            while(iterator_4.hasNext()) {
                String key_4 = iterator_4.next();
                if (key_4 == "t") {
                    residual = residual + flowHashMap.get(key_4);
                }
            }
        }
//		System.out.println(capacity);
        maximum_flow = capacity - residual;
        System.out.println("最大流为: "+maximum_flow);
    }
    public static void main(String args[])  {
        Dinic d = new Dinic();
        d.dinic_max_flow();
    }

}

