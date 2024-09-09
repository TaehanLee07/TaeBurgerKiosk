package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class TaeBurgerKiosk extends JFrame {
    private HashMap<String, Integer> menu = new HashMap<>();
    private HashMap<String, Integer> order = new HashMap<>();
    private int totalPrice = 0;
    private JTextArea orderArea;
    private JLabel totalLabel;

    public TaeBurgerKiosk() {
        // 메뉴 설정
        menu.put("수제버거", 6000);
        menu.put("수제치킨버거", 6500);
        menu.put("수제치즈버거", 6500);
        menu.put("수제더블버거", 7000);
        menu.put("스폐셜버거", 8000);

        setTitle("TaeBurger Kiosk");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 메뉴 패널
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(5, 1));
        for (String itemName : menu.keySet()) {
            JButton menuButton = new JButton(itemName + " - " + menu.get(itemName) + "원");
            menuButton.addActionListener(new AddOrderAction(itemName));
            menuPanel.add(menuButton);
        }
        add(menuPanel, BorderLayout.WEST);

        // 주문 내역 및 결제 패널
        JPanel orderPanel = new JPanel();
        orderPanel.setLayout(new BorderLayout());

        orderArea = new JTextArea();
        orderArea.setEditable(false);
        orderPanel.add(new JScrollPane(orderArea), BorderLayout.CENTER);

        totalLabel = new JLabel("총 가격: 0원");
        orderPanel.add(totalLabel, BorderLayout.SOUTH);

        JButton paymentButton = new JButton("결제");
        paymentButton.addActionListener(new PaymentAction());
        orderPanel.add(paymentButton, BorderLayout.EAST);

        add(orderPanel, BorderLayout.CENTER);
    }

    // 주문 추가 시 실행되는 이벤트
    private class AddOrderAction implements ActionListener {
        private String itemName;

        public AddOrderAction(String itemName) {
            this.itemName = itemName;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            order.put(itemName, order.getOrDefault(itemName, 0) + 1);
            totalPrice += menu.get(itemName);
            updateOrderDisplay();
        }
    }

    // 주문 목록 업데이트
    private void updateOrderDisplay() {
        StringBuilder orderText = new StringBuilder();
        for (String itemName : order.keySet()) {
            orderText.append(itemName)
                    .append(" x ")
                    .append(order.get(itemName))
                    .append("\n");
        }
        orderArea.setText(orderText.toString());
        totalLabel.setText("총 가격: " + totalPrice + "원");
    }

    // 결제 처리
    private class PaymentAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String[] paymentOptions = {"카드", "현금"};
            int paymentMethod = JOptionPane.showOptionDialog(null, "결제 방식을 선택하세요.", "결제",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, paymentOptions, paymentOptions[0]);

            if (paymentMethod == 0) { // 카드 결제
                String[] cardOptions = {"XX카드", "OO카드", "ㅁㅁ카드", "ZZ카드"};
                String selectedCard = (String) JOptionPane.showInputDialog(null, "무슨 카드로 하시겠습니까?", "카드 선택",
                        JOptionPane.QUESTION_MESSAGE, null, cardOptions, cardOptions[0]);

                JOptionPane.showMessageDialog(null, selectedCard + "으로 결제합니다.");
                Timer timer = new Timer(3000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JOptionPane.showMessageDialog(null, "결제 성공!");
                        resetOrder();
                    }
                });
                timer.setRepeats(false);
                timer.start();

            } else if (paymentMethod == 1) { // 현금 결제
                String cashInput = JOptionPane.showInputDialog("지불할 금액을 입력하세요:");
                int cashPaid = Integer.parseInt(cashInput);
                if (cashPaid >= totalPrice) {
                    int change = cashPaid - totalPrice;
                    JOptionPane.showMessageDialog(null, "영수증을 출력합니다. 거스름돈: " + change + "원");
                    Timer timer = new Timer(2000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            resetOrder();
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                } else {
                    JOptionPane.showMessageDialog(null, "지불 금액이 부족합니다.");
                }
            }
        }
    }

    // 주문 초기화
    private void resetOrder() {
        order.clear();
        totalPrice = 0;
        updateOrderDisplay();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TaeBurgerKiosk kiosk = new TaeBurgerKiosk();
            kiosk.setVisible(true);
        });
    }
}
