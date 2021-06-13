package io.github.kings1990.plugin.fastrequest.view;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.CollectionComboBoxModel;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.JBColor;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.dualView.TreeTableView;
import com.intellij.ui.table.JBTable;
import com.intellij.ui.treeStructure.treetable.ListTreeTableModelOnColumns;
import com.intellij.ui.treeStructure.treetable.TreeColumnInfo;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import icons.PluginIcons;
import io.github.kings1990.plugin.fastrequest.config.FastRequestComponent;
import io.github.kings1990.plugin.fastrequest.model.*;
import io.github.kings1990.plugin.fastrequest.util.KV;
import io.github.kings1990.plugin.fastrequest.util.ToolUtil;
import io.github.kings1990.plugin.fastrequest.util.TypeUtil;
import io.github.kings1990.plugin.fastrequest.util.UrlUtil;
import io.github.kings1990.plugin.fastrequest.view.inner.SupportView;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * FastRequest工具窗口
 *
 * @author Kings
 * @date 2021/06/02
 * @see SimpleToolWindowPanel
 */
public class FastRequestToolWindow extends SimpleToolWindowPanel {
    private static final Logger LOGGER = Logger.getInstance(CommonConfigView.class);
    public static final int JSON_TABLE_COLUMN_NAME_WIDTH = 200;
    public static final int JSON_TABLE_COLUMN_TYPE_WIDTH = 80;
    public static final int TYPE_WIDTH = 60;

    private JPanel panel;
    private JComboBox<String> envComboBox;
    private JComboBox<String> projectComboBox;
    private JCheckBox enableProjectCheckBox;
    private JButton starButton;
    private JButton coffeeMeButton;
    private JTextField currentDomainTextField;
    private JTextField urlTextField;
    private JTabbedPane paramTabbedPane;
    private JComboBox<String> methodTypeComboBox;
    private JTextArea urlParamsTextArea;
    private JTextArea jsonParamsTextArea;
    private JTextArea urlEncodedTextArea;
    private JTabbedPane urlEncodedTabbedPane;
    private JTabbedPane urlParamsTabbedPane;
    private JPanel urlParamsTablePanel;
    private JPanel urlEncodedTablePanel;
    private JScrollPane urlParamsTextPanel;
    private JScrollPane urlEncodedTextPanel;
    private JPanel pathParamsPanel;
    private JPanel jsonTreeTablePanel;
    private JTabbedPane jsonTabbedPanel;

    private JBTable urlParamsTable;
    private JBTable urlEncodedTable;
    private JBTable pathParamsTable;
    private TreeTableView jsonTreeTable;

    private List<ParamKeyValue> pathParamsKeyValueList = new ArrayList<>();
    private List<ParamKeyValue> urlParamsKeyValueList = new ArrayList<>();
    private List<ParamKeyValue> urlEncodedKeyValueList = new ArrayList<>();

    private AtomicBoolean urlEncodedParamChangeFlag;
    private AtomicBoolean urlParamsChangeFlag;


    private static final Map<Object, Icon> TYPE_ICONS = ImmutableMap.<Object, Icon>builder()
            .put(TypeUtil.Type.Object.name(), PluginIcons.ICON_OBJECT)
            .put(TypeUtil.Type.Array.name(), PluginIcons.ICON_ARRAY)
            .put(TypeUtil.Type.String.name(), PluginIcons.ICON_STRING)
            .put(TypeUtil.Type.Number.name(), PluginIcons.ICON_NUMBER)
            .put(TypeUtil.Type.Boolean.name(), PluginIcons.ICON_BOOLEAN)
            .build();
    private ComboBox<String> typeJComboBox;
    private ComboBox<String> normalTypeJComboBox;

    private ComboBox getTypeComboBox(String type) {
        ComboBox<String> typeJComboBox = new ComboBox<>();
        typeJComboBox.setRenderer(new IconListRenderer(TYPE_ICONS));
        typeJComboBox.addItem(TypeUtil.Type.Number.name());
        typeJComboBox.addItem(TypeUtil.Type.Array.name());
        typeJComboBox.addItem(TypeUtil.Type.String.name());
        typeJComboBox.addItem(TypeUtil.Type.Object.name());
        typeJComboBox.addItem(TypeUtil.Type.Boolean.name());
        typeJComboBox.setSelectedItem(type);
        return typeJComboBox;
    }

    private ComboBox getNormalTypeComboBox(String type) {
        ComboBox<String> typeJComboBox = new ComboBox<>();
        typeJComboBox.setRenderer(new IconListRenderer(TYPE_ICONS));
        typeJComboBox.addItem(TypeUtil.Type.Number.name());
        typeJComboBox.addItem(TypeUtil.Type.String.name());
        typeJComboBox.addItem(TypeUtil.Type.Boolean.name());
        typeJComboBox.setSelectedItem(type);
        return typeJComboBox;
    }

    private void createUIComponents() {
        typeJComboBox = new ComboBox<>();
        typeJComboBox.setRenderer(new IconListRenderer(TYPE_ICONS));
        typeJComboBox.addItem(TypeUtil.Type.Number.name());
        typeJComboBox.addItem(TypeUtil.Type.Array.name());
        typeJComboBox.addItem(TypeUtil.Type.String.name());
        typeJComboBox.addItem(TypeUtil.Type.Object.name());
        typeJComboBox.addItem(TypeUtil.Type.Boolean.name());

        normalTypeJComboBox = new ComboBox<>();
        normalTypeJComboBox.setRenderer(new IconListRenderer(TYPE_ICONS));
        normalTypeJComboBox.addItem(TypeUtil.Type.Number.name());
        normalTypeJComboBox.addItem(TypeUtil.Type.String.name());
        normalTypeJComboBox.addItem(TypeUtil.Type.Boolean.name());

        urlEncodedParamChangeFlag = new AtomicBoolean(true);
        urlParamsChangeFlag = new AtomicBoolean(true);
        renderingUrlParamsTablePanel();
        renderingUrlEncodedPanel();
        renderingPathParamsPanel();
        renderingJsonTreeTablePanel();
    }

    public FastRequestToolWindow(ToolWindow toolWindow) {
        super(true, false);
        this.setContent(panel);
        FastRequestConfiguration config = FastRequestComponent.getInstance().getState();
        assert config != null;
        //star
        starButton.addActionListener(event -> {
            try {
                Desktop dp = Desktop.getDesktop();
                if (dp.isSupported(Desktop.Action.BROWSE)) {
                    dp.browse(URI.create("https://github.com/kings1990/fast-request"));
                }
            } catch (Exception e) {
                LOGGER.error("open url fail:https://github.com/kings1990/fast-request", e);
            }
        });

        //coffee
        coffeeMeButton.addActionListener(event -> {
            SupportView supportView = new SupportView();
            supportView.show();
        });

        //env下拉列表
        CollectionComboBoxModel<String> envModel = new CollectionComboBoxModel<>(config.getEnvList());
        envComboBox.setModel(envModel);

        //method 颜色渲染
        methodTypeComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (c instanceof JLabel) {
                    JLabel l = (JLabel) c;
                    l.setBackground(buildMethodColor(value.toString()));
                }
                return c;
            }
        });

        methodTypeComboBox.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                Object selectedItem = methodTypeComboBox.getSelectedItem();
                if (selectedItem != null) {
                    methodTypeComboBox.setBackground(buildMethodColor(selectedItem.toString()));
                }
            }
        });
        methodTypeComboBox.setBackground(JBColor.BLUE);


        envComboBox.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                Object selectEnv = envComboBox.getSelectedItem();
                if (selectEnv == null) {
                    return;
                }
                String env = selectEnv.toString();
                List<String> envList = config.getEnvList();
                if (!envList.contains(env)) {
                    //配置删除了当前的env则默认选中第一个env
                    if (envList.isEmpty()) {
                        //env被删除完了 补全域名开关自动关闭
                        config.setEnableEnv(null);
                        config.setEnableFlag(false);
                        config.setDomain(StringUtils.EMPTY);
                        enableProjectCheckBox.setSelected(false);
                    } else {
                        config.setEnableEnv(envList.get(0));
                    }
                    envModel.setSelectedItem(config.getEnableEnv());
                } else {
                    config.setEnableEnv(env);
                }
                //根据当前的env和project设置url
                setDomain(config);
            }
        });
        envModel.setSelectedItem(config.getEnableEnv());

        //project下拉列表
        CollectionComboBoxModel<String> projectModel = new CollectionComboBoxModel<>(config.getProjectList());
        projectComboBox.setModel(projectModel);
        projectComboBox.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                Object selectProject = projectComboBox.getSelectedItem();
                if (selectProject == null) {
                    return;
                }
                String project = selectProject.toString();
                List<String> projectList = config.getProjectList();
                if (!projectList.contains(project)) {
                    //配置删除了当前的env则默认选中第一个env
                    if (projectList.isEmpty()) {
                        //project被删除完了 补全域名开关自动关闭
                        config.setEnableProject(null);
                        config.setEnableFlag(false);
                        config.setDomain(StringUtils.EMPTY);
                        enableProjectCheckBox.setSelected(false);
                    } else {
                        config.setEnableProject(projectList.get(0));
                    }
                    projectModel.setSelectedItem(config.getEnableProject());
                } else {
                    config.setEnableProject(project);
                }
                //根据当前的env和project设置url
                setDomain(config);
            }
        });
        projectModel.setSelectedItem(config.getEnableProject());

        //选中事件
        enableProjectCheckBox.setSelected(config.isEnableFlag());
        enableProjectCheckBox.addItemListener(event -> {
            boolean enabled = enableProjectCheckBox.isSelected();
            config.setEnableFlag(enabled);
            setDomain(config);
        });

        //更新域名
        config.getParamGroup().setOriginUrl("");
        setDomain(config);

        //动态更新text中的内容
        urlEncodedTabbedPane.addChangeListener(changeEvent -> {
            if (urlEncodedTabbedPane.getSelectedIndex() == 1 && urlEncodedParamChangeFlag.get()) {
                //table change 引发重新构建text
                String paramStr = conventDataToString(urlEncodedKeyValueList);
                urlEncodedTextArea.setText(paramStr);
                urlEncodedParamChangeFlag.set(false);
            }
            if(urlEncodedTabbedPane.getSelectedIndex() == 0){
                String paramStr = conventDataToString(urlEncodedKeyValueList);
                String currentUrlParamText = urlEncodedTextArea.getText();
                if(!paramStr.equals(currentUrlParamText)){
                    String[] split = currentUrlParamText.split("&");
                    List<ParamKeyValue> currentUrlParamsKeyValueList = new ArrayList<>();
                    for (String s : split) {
                        String[] kvArray = s.split("=");
                        if(kvArray.length == 2){
                            String value = kvArray[1].replace("\n", "");
                            ParamKeyValue paramKeyValue = new ParamKeyValue(kvArray[0],kvArray[1],2,TypeUtil.calcTypeByStringValue(value));
                            currentUrlParamsKeyValueList.add(paramKeyValue);
                        }
                    }
                    urlEncodedKeyValueList = currentUrlParamsKeyValueList;
                    urlEncodedTable.setModel(new ListTableModel<>(getPathColumnInfo(), urlEncodedKeyValueList));
                    setUpNormalTypeColumn(urlEncodedTable.getColumnModel().getColumn(0));
                    resizeTable(urlEncodedTable);
                }
            }
        });

        //动态更新text中的内容
        urlParamsTabbedPane.addChangeListener(changeEvent -> {
            //table change 引发重新构建text
            if (urlParamsTabbedPane.getSelectedIndex() == 1 && urlParamsChangeFlag.get()) {
                String paramStr = conventDataToString(urlParamsKeyValueList);
                urlParamsTextArea.setText(paramStr);
                urlParamsChangeFlag.set(false);
            }
            if(urlParamsTabbedPane.getSelectedIndex() == 0){
                String paramStr = conventDataToString(urlParamsKeyValueList);
                String currentUrlParamText = urlParamsTextArea.getText();
                if(!paramStr.equals(currentUrlParamText)){
                    String[] split = currentUrlParamText.split("&");
                    List<ParamKeyValue> currentUrlParamsKeyValueList = new ArrayList<>();
                    for (String s : split) {
                        String[] kvArray = s.split("=");
                        if(kvArray.length == 2){
                            String value = kvArray[1].replace("\n", "");
                            ParamKeyValue paramKeyValue = new ParamKeyValue(kvArray[0],kvArray[1],2,TypeUtil.calcTypeByStringValue(value));
                            currentUrlParamsKeyValueList.add(paramKeyValue);
                        }
                    }
                    urlParamsKeyValueList = currentUrlParamsKeyValueList;
                    urlParamsTable.setModel(new ListTableModel<>(getPathColumnInfo(), urlParamsKeyValueList));
                    setUpNormalTypeColumn(urlParamsTable.getColumnModel().getColumn(0));
                    resizeTable(urlParamsTable);
                }
            }
        });

        //copy param
        jsonParamsTextArea.addMouseListener(copyMouseAdapter(jsonParamsTextArea));
        urlEncodedTextArea.addMouseListener(copyMouseAdapter(urlEncodedTextArea));
        urlParamsTextArea.addMouseListener(copyMouseAdapter(urlParamsTextArea));
        urlTextField.addMouseListener(copyMouseAdapterField(urlTextField));
    }

    /**
     * text鼠标右键拷贝至粘贴板
     *
     * @param textarea 文本区域
     * @return {@link MouseAdapter }
     * @author Kings
     * @date 2021/06/07
     */
    private MouseAdapter copyMouseAdapter(JTextArea textarea) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String text = textarea.getText();
                if (SwingUtilities.isRightMouseButton(e) && StringUtils.isNotEmpty(text)) {
                    ToolUtil.setClipboardString(text);
                    MyNotifier.notifyCopy();
                }
            }
        };
    }

    private MouseAdapter copyMouseAdapterField(JTextField textField) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String text = textField.getText();
                if (SwingUtilities.isRightMouseButton(e) && StringUtils.isNotEmpty(text)) {
                    ToolUtil.setClipboardString(text);
                    MyNotifier.notifyCopy();
                }
            }
        };
    }

    /**
     * 根据配置设置domain
     *
     * @param config 配置
     * @author Kings
     * @date 2021/05/23
     */
    private void setDomain(FastRequestConfiguration config) {
        if (!config.isEnableFlag()) {
            //没有开启开关则url默认返回空字符串
            config.setDomain(StringUtils.EMPTY);
            currentDomainTextField.setText(StringUtils.EMPTY);
            changeUrl();
            return;
        }
        String activeEnv = config.getEnableEnv();
        String activeProject = config.getEnableProject();
        if (StringUtils.isEmpty(activeEnv)) {
            config.setDomain(StringUtils.EMPTY);
            return;
        }
        if (StringUtils.isEmpty(activeProject)) {
            config.setDomain(StringUtils.EMPTY);
            return;
        }
        NameGroup defaultNameGroup = new NameGroup(StringUtils.EMPTY, new ArrayList<>());
        HostGroup defaultHostGroup = new HostGroup(StringUtils.EMPTY, StringUtils.EMPTY);
        String domain = config.getDataList().stream().filter(n -> activeProject.equals(n.getName())).findFirst().orElse(defaultNameGroup)
                .getHostGroup().stream().filter(h -> activeEnv.equals(h.getEnv())).findFirst().orElse(defaultHostGroup).getUrl();
        config.setDomain(domain);
        currentDomainTextField.setText(domain);
        changeUrl();
    }

    /**
     * message事件:修改env和project动态修改ToolWindow中的内容
     *
     * @author Kings
     * @date 2021/06/02
     */
    public void changeEnvAndProject() {
        FastRequestConfiguration config = FastRequestComponent.getInstance().getState();
        assert config != null;

        CollectionComboBoxModel<String> projectModel = new CollectionComboBoxModel<>(config.getProjectList());
        projectComboBox.setModel(projectModel);

        CollectionComboBoxModel<String> envModel = new CollectionComboBoxModel<>(config.getEnvList());
        envComboBox.setModel(envModel);

        projectComboBox.setSelectedItem(config.getEnableProject());
        envComboBox.setSelectedItem(config.getEnableEnv());
        enableProjectCheckBox.setSelected(config.isEnableFlag());
        setDomain(config);
    }

    private JBColor buildMethodColor(String method) {
        if ("POST".equals(method)) {
            return JBColor.GREEN;
        } else if ("DELETE".equals(method)) {
            return JBColor.RED;
        } else if ("PUT".equals(method)) {
            return JBColor.YELLOW;
        } else if ("GET".equals(method)) {
            return JBColor.BLUE;
        }
        return JBColor.darkGray;
    }

    /**
     * message事件:action操作生成数据,修改ToolWindow中的内容
     *
     * @author Kings
     * @date 2021/06/02
     */
    public void refresh() {
        FastRequestConfiguration config = FastRequestComponent.getInstance().getState();
        assert config != null;
        ParamGroup paramGroup = config.getParamGroup();
        LinkedHashMap<String, Object> pathParamMap = paramGroup.getPathParamMap();
        LinkedHashMap<String, Object> requestParamMap = paramGroup.getRequestParamMap();
        LinkedHashMap<String, Object> bodyParamMap = paramGroup.getBodyParamMap();
        String methodType = paramGroup.getMethodType();


        methodTypeComboBox.setBackground(buildMethodColor(methodType));

        //method
        methodTypeComboBox.setSelectedItem(methodType);

        //request param

        String requestParamStr = conventDataToString(conventMapToList(requestParamMap));

        //默认urlParam是允许的即使是post json形式
        urlParamsKeyValueList = conventMapToList(requestParamMap);
        pathParamsKeyValueList = conventMapToList(pathParamMap);
//        pathParamsKeyValueList = conventPathParamsToList(pathParamMap);

        if ("GET".equals(methodType)) {
            urlParamsTextArea.setText(requestParamStr);
            paramTabbedPane.setSelectedIndex(0);
            urlParamsTabbedPane.setSelectedIndex(0);
            //get请求urlencoded param参数为空
            urlEncodedKeyValueList = new ArrayList<>();
            urlEncodedTextArea.setText("");
            jsonParamsTextArea.setText("");
        } else {
            //body param
            if (!bodyParamMap.isEmpty()) {
                //json
                jsonParamsTextArea.setText(bodyParamMapToJson(bodyParamMap));
                paramTabbedPane.setSelectedIndex(1);
                jsonTabbedPanel.setSelectedIndex(0);
                urlEncodedTextArea.setText("");
                urlEncodedKeyValueList = new ArrayList<>();
            } else {
                //urlencoded
                urlEncodedTextArea.setText(requestParamStr);
                paramTabbedPane.setSelectedIndex(2);
                urlEncodedTabbedPane.setSelectedIndex(0);
                urlEncodedKeyValueList = conventMapToList(requestParamMap);
                //json设置为空
                jsonParamsTextArea.setText("");

                //如果是非get请求则request Param为空转到url Encoded参数下
                urlParamsKeyValueList = new ArrayList<>();
                urlParamsTextArea.setText("");
            }
        }
        //刷新table
        TableColumnModel pathParamColumnModel = pathParamsTable.getColumnModel();
        pathParamsTable.setModel(new ListTableModel<>(getPathColumnInfo(), pathParamsKeyValueList));
        setUpNormalTypeColumn(pathParamColumnModel.getColumn(0));
        resizeTable(pathParamsTable);

        TableColumnModel urlParamColumnModel = urlParamsTable.getColumnModel();
        urlParamsTable.setModel(new ListTableModel<>(getPathColumnInfo(), urlParamsKeyValueList));
        setUpNormalTypeColumn(urlParamColumnModel.getColumn(0));
        resizeTable(urlParamsTable);

        TableColumnModel urlEncodedColumnModel = urlEncodedTable.getColumnModel();
        urlEncodedTable.setModel(new ListTableModel<>(getPathColumnInfo(), urlEncodedKeyValueList));
        setUpNormalTypeColumn(urlEncodedColumnModel.getColumn(0));
        resizeTable(urlEncodedTable);

        //json table
        CustomNode root = new CustomNode("Root", "", TypeUtil.Type.Object.name());
        convertToNode(root, bodyParamMap);
        ListTreeTableModelOnColumns treeModel = new ListTreeTableModelOnColumns(root, jsonColumnInfo());
        jsonTreeTable.setModel(treeModel);
        //列宽
        TableColumnModel columnModel = jsonTreeTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(JSON_TABLE_COLUMN_NAME_WIDTH);
        columnModel.getColumn(1).setPreferredWidth(JSON_TABLE_COLUMN_TYPE_WIDTH);
        expandAll(jsonTreeTable.getTree(), new TreePath(root), true);
        jsonTreeTable.setTreeCellRenderer(new Renderer());
//        setUpTypeColumn(jsonTreeTable.getColumnModel().getColumn(1));
        setDomain(config);

    }

    public void resizeTable(JBTable table) {
        table.getColumnModel().getColumn(0).setPreferredWidth((int) Math.round(table.getWidth() * 0.2));
        table.getColumnModel().getColumn(1).setPreferredWidth((int) Math.round(table.getWidth() * 0.3));
        table.getColumnModel().getColumn(2).setPreferredWidth((int) Math.round(table.getWidth() * 0.5));
    }


    private void changeUrl() {
        FastRequestConfiguration config = FastRequestComponent.getInstance().getState();
        assert config != null;
        ParamGroup paramGroup = config.getParamGroup();
        String originUrl = paramGroup.getOriginUrl();
        String url = buildPathParamUrl(originUrl);
        String domain = config.getDomain();
        url = domain + ((url.startsWith("/") || "".equals(url)) ? "" : "/") + url;
        urlTextField.setText(url);
        paramGroup.setUrl(url);
    }

    private String buildPathParamUrl(String url) {
        List<String> paramNameList = UrlUtil.paramPathParam(url);
        if (paramNameList.isEmpty()) {
            return url;
        }
        for (ParamKeyValue paramKeyValue : pathParamsKeyValueList) {
            String paramName = paramKeyValue.getKey();
            String paramNameWithSymbol = "{" + paramName + "}";
            url = url.replace(paramNameWithSymbol, paramKeyValue.getValue().toString());
        }
        return url;
    }


    /**
     * 渲染UrlParams table面板
     *
     * @author Kings
     * @date 2021/06/02
     */
    private void renderingUrlParamsTablePanel() {
        FastRequestConfiguration config = FastRequestComponent.getInstance().getState();
        assert config != null;
        ParamGroup paramGroup = config.getParamGroup();
        String methodType = paramGroup.getMethodType();
        if (!"GET".equals(methodType)) {
            urlParamsKeyValueList = new ArrayList<>();
        }
        urlParamsTable = createUrlParamsKeyValueTable();
        urlParamsTable.getEmptyText().setText("No params");
        ToolbarDecorator toolbarDecorator = ToolbarDecorator.createDecorator(urlParamsTable);
        toolbarDecorator.setMoveDownAction(null);
        toolbarDecorator.setMoveUpAction(null);

        toolbarDecorator.setAddAction(anActionButton -> {
                    int selectedRow = urlParamsTable.getSelectedRow();
                    if (selectedRow == -1) {
                        urlParamsKeyValueList.add(new ParamKeyValue("", "", 2, TypeUtil.Type.String.name()));
                    } else {
                        urlParamsKeyValueList.add(selectedRow + 1, new ParamKeyValue("", "", 2, TypeUtil.Type.String.name()));
                    }
                    urlParamsTable.setModel(new ListTableModel<>(getPathColumnInfo(), urlParamsKeyValueList));
                    setUpNormalTypeColumn(urlParamsTable.getColumnModel().getColumn(0));
                    resizeTable(urlParamsTable);
                }
        ).setRemoveAction(anActionButton -> {
            int selectedRow = urlParamsTable.getSelectedRow();
            urlParamsKeyValueList.remove(selectedRow);
            urlParamsTable.setModel(new ListTableModel<>(getPathColumnInfo(), urlParamsKeyValueList));
            setUpNormalTypeColumn(urlParamsTable.getColumnModel().getColumn(0));
            resizeTable(urlParamsTable);
        });
        urlParamsTablePanel = toolbarDecorator.createPanel();
    }

    private void renderingJsonTreeTablePanel() {
        jsonTreeTable = createJsonParamKeyValueTable();
        expandAll(jsonTreeTable.getTree(), new TreePath(jsonTreeTable.getTableModel().getRoot()), true);
        jsonTreeTable.setTreeCellRenderer(new Renderer());
//        setUpTypeColumn(jsonTreeTable.getColumnModel().getColumn(1));
        ToolbarDecorator toolbarDecorator = ToolbarDecorator.createDecorator(jsonTreeTable);
        toolbarDecorator.setMoveDownAction(null);
        toolbarDecorator.setMoveUpAction(null);
        toolbarDecorator.setAddAction(null);
        toolbarDecorator.setRemoveAction(null);
        jsonTreeTablePanel = toolbarDecorator.createPanel();

    }

    private void renderingPathParamsPanel() {
        pathParamsTable = createPathParamKeyValueTable();
        pathParamsTable.getEmptyText().setText("No params");
        ToolbarDecorator toolbarDecorator = ToolbarDecorator.createDecorator(pathParamsTable);
        toolbarDecorator.setMoveDownAction(null);
        toolbarDecorator.setMoveUpAction(null);

        toolbarDecorator.setAddAction(anActionButton -> {
                    int selectedRow = pathParamsTable.getSelectedRow();
                    if (selectedRow == -1) {
                        pathParamsKeyValueList.add(new ParamKeyValue("", "", 2, TypeUtil.Type.String.name()));
                    } else {
                        pathParamsKeyValueList.add(selectedRow + 1, new ParamKeyValue("", "", 2, TypeUtil.Type.String.name()));
                    }
                    pathParamsTable.setModel(new ListTableModel<>(getPathColumnInfo(), pathParamsKeyValueList));
                    setUpNormalTypeColumn(pathParamsTable.getColumnModel().getColumn(0));
                    resizeTable(pathParamsTable);
                    changeUrl();
                }
        ).setRemoveAction(anActionButton -> {
            int selectedRow = pathParamsTable.getSelectedRow();
            pathParamsKeyValueList.remove(selectedRow);
            pathParamsTable.setModel(new ListTableModel<>(getPathColumnInfo(), pathParamsKeyValueList));
            setUpNormalTypeColumn(pathParamsTable.getColumnModel().getColumn(0));
            resizeTable(pathParamsTable);
            changeUrl();
        });
        pathParamsPanel = toolbarDecorator.createPanel();
    }

    /**
     * 渲染UrlEncoded table面板
     *
     * @author Kings
     * @date 2021/06/02
     */
    private void renderingUrlEncodedPanel() {
        FastRequestConfiguration config = FastRequestComponent.getInstance().getState();
        assert config != null;
        ParamGroup paramGroup = config.getParamGroup();
        LinkedHashMap<String, Object> bodyParamMap = paramGroup.getBodyParamMap();
        String methodType = paramGroup.getMethodType();
        if (!"GET".equals(methodType)) {
            //body param
            Object bodyParam = bodyParamMap.values().stream().findFirst().orElse("");
            if ("".equals(bodyParam)) {
                //json形式 urlencoded 值为空
                urlEncodedKeyValueList = new ArrayList<>();
            }
        } else {
            //get urlencoded 值为空
            urlEncodedKeyValueList = new ArrayList<>();
        }
        urlEncodedTable = createUrlEncodedKeyValueTable();
        urlEncodedTable.getEmptyText().setText("No params");
        ToolbarDecorator toolbarDecorator = ToolbarDecorator.createDecorator(urlEncodedTable);
        toolbarDecorator.setMoveDownAction(null);
        toolbarDecorator.setMoveUpAction(null);

        toolbarDecorator.setAddAction(anActionButton -> {
                    int selectedRow = urlEncodedTable.getSelectedRow();
                    if (selectedRow == -1) {
                        urlEncodedKeyValueList.add(new ParamKeyValue("", "", 2, TypeUtil.Type.String.name()));
                    } else {
                        urlEncodedKeyValueList.add(selectedRow + 1, new ParamKeyValue("", "", 2, TypeUtil.Type.String.name()));
                    }
                    urlEncodedTable.setModel(new ListTableModel<>(getPathColumnInfo(), urlEncodedKeyValueList));
                    setUpNormalTypeColumn(urlEncodedTable.getColumnModel().getColumn(0));
                    resizeTable(urlEncodedTable);
                }
        ).setRemoveAction(anActionButton -> {
            int selectedRow = urlEncodedTable.getSelectedRow();
            urlEncodedKeyValueList.remove(selectedRow);
            pathParamsTable.setModel(new ListTableModel<>(getPathColumnInfo(), urlEncodedKeyValueList));
            setUpNormalTypeColumn(urlEncodedTable.getColumnModel().getColumn(0));
            resizeTable(urlEncodedTable);
        });
        urlEncodedTablePanel = toolbarDecorator.createPanel();
    }

    /**
     * LinkedHashMap数据转ParamKeyValue集合
     *
     * @param paramLinkedMap 参数与地图
     * @return {@link List<ParamKeyValue> }
     * @author Kings
     * @date 2021/06/02
     */
    private List<ParamKeyValue> conventMapToList(LinkedHashMap<String, Object> paramLinkedMap) {
        List<ParamKeyValue> paramKeyValueList = new ArrayList<>();
        paramLinkedMap.forEach((key, value) -> {
            ParamKeyValue paramKeyValue = (ParamKeyValue) value;
            if (paramKeyValue.getCustomFlag() == 1) {
                KV<String, ParamKeyValue> data = (KV<String, ParamKeyValue>) paramKeyValue.getValue();
                //kv转成普通类型
                List<ParamKeyValue> list = new ArrayList<>();
                convertToParamKeyValueList("", data, list);
                paramKeyValueList.addAll(list);
            } else {
                paramKeyValueList.add(paramKeyValue);
            }
        });
        return paramKeyValueList;
    }

    private List<ParamKeyValue> conventPathParamsToList(LinkedHashMap<String, Object> paramLinkedMap) {
        List<ParamKeyValue> paramKeyValueList = new ArrayList<>();
        paramLinkedMap.forEach((key, value) -> paramKeyValueList.add((ParamKeyValue) value));
        return paramKeyValueList;
    }

    /**
     * requestParam urlEncodedParam 转 text
     * 每个参数换行处理
     *
     * @param paramKeyValueList 参数键值列表
     * @return {@link String }
     * @author Kings
     * @date 2021/06/02
     */
    private String conventDataToString(List<ParamKeyValue> paramKeyValueList) {
        StringBuilder sb = new StringBuilder();
        paramKeyValueList.forEach(paramKeyValue -> {
            if (paramKeyValue.getCustomFlag() == 2) {
                //基本类型映射  key=value
                sb.append(paramKeyValue.getKey()).append("=").append(paramKeyValue.getValue()).append("\n&");
            } else {
                //对象 直接拼上value
                sb.append(paramKeyValue.getValue()).append("\n&");
            }
        });
        return StringUtils.removeEnd(sb.toString(), "\n&");
    }


    private ColumnInfo<Object, Object>[] getPathColumnInfo() {
        ColumnInfo<Object, Object>[] columnArray = new ColumnInfo[3];
        List<String> titleList = Lists.newArrayList("Type", "Key", "Value");
        for (int i = 0; i < titleList.size(); i++) {
            ColumnInfo<Object, Object> envColumn = new ColumnInfo<>(titleList.get(i)) {
                @Override
                public @Nullable Object valueOf(Object o) {
                    return o;
                }
            };

            columnArray[i] = envColumn;
        }
        return columnArray;
    }

    /**
     * table列信息
     *
     * @return {@link ColumnInfo<Object, Object>[] }
     * @author Kings
     * @date 2021/06/02
     */
    private ColumnInfo<Object, Object>[] getColumnInfo() {
        ColumnInfo<Object, Object>[] columnArray = new ColumnInfo[2];
        List<String> titleList = Lists.newArrayList("Key", "Value");
        for (int i = 0; i < titleList.size(); i++) {
            ColumnInfo<Object, Object> envColumn = new ColumnInfo<>(titleList.get(i)) {
                @Override
                public @Nullable Object valueOf(Object o) {
                    return o;
                }
            };

            columnArray[i] = envColumn;
        }
        return columnArray;
    }

    private TreeTableView createJsonParamKeyValueTable() {
        //初始化为空
        LinkedHashMap<String, Object> bodyParamMap = new LinkedHashMap<>();

        CustomNode root = new CustomNode("Root", "", TypeUtil.Type.Object.name());
        convertToNode(root, bodyParamMap);
        ListTreeTableModelOnColumns model = new ListTreeTableModelOnColumns(root, jsonColumnInfo());
        TreeTableView table = new TreeTableView(model) {
            @Override
            public void setTreeCellRenderer(TreeCellRenderer renderer) {
                super.setTreeCellRenderer(new Renderer());
            }

//            @Override
//            public @NotNull Component prepareRenderer(@NotNull TableCellRenderer renderer, int row, int column) {
//                if(column == 1){
//                    CustomNode node = (CustomNode) getValueAt(row, column);
//                    String type = node.getType();
//                    return getTypeComboBox(type);
//                }
//                return super.prepareRenderer(renderer, row, column);
//            }

            @Override
            public boolean isCellEditable(int row, int column) {
                ListTreeTableModelOnColumns myModel = (ListTreeTableModelOnColumns) getTableModel();
                CustomNode node = (CustomNode) myModel.getRowValue(row);
                return !node.isRoot() && node.isLeaf() && column == 1;
            }

            @Override
            public void setCellEditor(TableCellEditor anEditor) {
                super.setCellEditor(anEditor);
            }

//            @Override
//            public Class<?> getColumnClass(int column) {
//                if(column == 1){
//                    return Icon.class;
//                }
//                return super.getColumnClass(column);
//            }

            @Override
            public void setValueAt(Object value, int row, int column) {
                if (column == 1) {
                    boolean changeFlag = false;
                    ListTreeTableModelOnColumns myModel = (ListTreeTableModelOnColumns) getTableModel();
                    CustomNode node = (CustomNode) myModel.getRowValue(row);
                    TreeNode[] nodePath = node.getPath();
                    if (!node.getValue().equals(value)) {
                        changeFlag = true;
                    }
                    String type = node.getType();
                    node.setValue(value);
                    if (changeFlag) {
                        //更新bodyParamMap
                        updateBodyParamMap(nodePath, value, type);
                    }
                }
            }
        };
        table.setRootVisible(true);
        table.setVisible(true);
        return table;
    }

    private void updateBodyParamMap(TreeNode[] nodePath, Object value, String type) {
        FastRequestConfiguration config = FastRequestComponent.getInstance().getState();
        assert config != null;
        ParamGroup paramGroup = config.getParamGroup();
        LinkedHashMap<String, Object> bodyParamMap = paramGroup.getBodyParamMap();
        String key;
        ParamKeyValue kv = null;
        for (int i = 1; i < nodePath.length; i++) {
            //i=1 排除root节点
            CustomNode node = (CustomNode) nodePath[i];
            key = node.getKey();
            if (TypeUtil.Type.Array.name().equals(node.getType()) && key.startsWith("index ")) {
                //跳过数组
                continue;
            }
            Object o;
            if (kv == null) {
                //第一个节点
                o = bodyParamMap.get(key);
            } else {
                o = kv.getValue();
            }
            if (o instanceof KV) {
                kv = (ParamKeyValue) ((KV) o).get(key);
            } else if (o instanceof ArrayList) {
                kv = (ParamKeyValue) ((KV) ((ArrayList) o).get(0)).get(key);
            } else {
                kv = (ParamKeyValue) o;
            }
        }
        kv.setValue(convertCellData(value, type));
        changeJsonText(bodyParamMap);
    }

    /**
     * 解析数据,异常返回默认值
     *
     * @param type 类型
     * @return {@link Object }
     * @author Kings
     * @date 2021/06/09
     */
    private Object convertCellData(Object toBeConvert, String type) {
        Object defaultValue = null;
        try {

            switch (type) {
                case "Number":
                    defaultValue = 1;
                    break;
                case "Boolean":
                    defaultValue = true;
                    break;
                default:
                    defaultValue = "";
                    break;
            }
            if (TypeUtil.Type.Number.name().equals(type)) {
                return Integer.parseInt(toBeConvert.toString());
            } else if (TypeUtil.Type.Boolean.name().equals(type)) {
                return Boolean.parseBoolean(toBeConvert.toString());
            } else {
                return toBeConvert;
            }
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private String bodyParamMapToJson(LinkedHashMap<String, Object> bodyParamMap) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        convertToMap(bodyParamMap, map);
        return JSON.toJSONString(map, true);
    }

    private void changeJsonText(LinkedHashMap<String, Object> bodyParamMap) {
        //json
        jsonParamsTextArea.setText(bodyParamMapToJson(bodyParamMap));
    }

    public void setUpTypeColumn(TableColumn typeColumn) {
        typeColumn.setCellEditor(new DefaultCellEditor(typeJComboBox));
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        typeColumn.setCellRenderer(renderer);
    }

    public void setUpNormalTypeColumn(TableColumn typeColumn) {
        typeColumn.setCellEditor(new DefaultCellEditor(normalTypeJComboBox));
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        typeColumn.setCellRenderer(renderer);
    }

    class Renderer extends ColoredTreeCellRenderer {
        @Override
        public void customizeCellRenderer(@NotNull JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            //解决TreeTable key加上>
            CustomNode node = (CustomNode) value;
//            setBorder(BorderFactory.createLineBorder(JBColor.BLACK, 3));
            append(node.getKey());

            setIcon(TYPE_ICONS.get(node.getType()));
        }
    }

    class IconListRenderer extends DefaultListCellRenderer {
        private static final long serialVersionUID = 1L;
        private Map<Object, Icon> icons = null;

        public IconListRenderer(Map<Object, Icon> icons) {
            this.icons = icons;
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Icon icon = icons.get(value);
            JLabel picture = new JLabel("", icon, JLabel.LEFT);
            if (index != -1) {
                //下拉才显示值
                picture.setText(value.toString());
            }
            picture.setHorizontalAlignment(JLabel.LEFT);
            return picture;
        }


    }

    private static void expandAll(JTree tree, TreePath parent, boolean expand) {
        // Traverse children
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }

        // Expansion or collapse must be done bottom-up
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }

    private ColumnInfo[] jsonColumnInfo() {
        ColumnInfo value = new ColumnInfo("Value") {
            @Nullable
            @Override
            public Object valueOf(Object o) {
                if (o instanceof CustomNode) {
                    return ((CustomNode) o).getValue();
                } else return o;
            }
        };
//        ColumnInfo type = new ColumnInfo("Type") {
//            @Nullable
//            @Override
//            public Object valueOf(Object o) {
//                return o;
//            }
//            @Override
//            public int getWidth(JTable table) {
//                return JSON_TABLE_COLUMN_TYPE_WIDTH;
//            }
//        };

        ColumnInfo[] columnInfos = new ColumnInfo[]{
                new TreeColumnInfo("Name") {
                    @Override
                    public int getWidth(JTable table) {
                        return JSON_TABLE_COLUMN_NAME_WIDTH;
                    }
                },   // <-- This is important!
//                type,
                value
        };
        return columnInfos;
    }

//    private CustomNode convertJsonObjectToNode(CustomNode node, JSONObject jsonObject) {
//        LinkedHashMap<String, Object> linkedHashMap = JSON.parseObject(JSON.toJSONString(jsonObject), new TypeReference<LinkedHashMap<String, Object>>() {
//        });
//        Set<String> keys = linkedHashMap.keySet();
//        keys.forEach(key -> {
////            node.setKey(key);
//            Object value = jsonObject.get(key);
//            if (value instanceof JSONObject) {
//                JSONObject valueJsonObject = (JSONObject) value;
//                CustomNode customNode = new CustomNode(key, null);
//                node.add(convertJsonObjectToNode(customNode, valueJsonObject));
//            } else if (value instanceof JSONArray) {
//                JSONArray jsonArray = (JSONArray) value;
//                if (jsonArray.size() == 0) {
//                    return;
//                }
//                convertJsonArrayToNode(key, jsonArray, node);
//            } else {
//                node.add(new CustomNode(key, value));
//            }
//        });
//        return node;
//    }


    /**
     * json数据转化为map用于text展示(递归遍历)
     *
     * @param data   数据
     * @param result 结果
     * @return
     * @author Kings
     * @date 2021/06/07
     */
    private void convertToMap(LinkedHashMap<String, Object> data, LinkedHashMap<String, Object> result) {
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            ParamKeyValue value = (ParamKeyValue) entry.getValue();
            String type = value.getType();
            Object dataValue = value.getValue();
            if (TypeUtil.Type.Object.name().equals(type)) {
                LinkedHashMap<String, Object> objectLinkedHashMap = new LinkedHashMap<>();
                LinkedHashMap<String, Object> kv = (LinkedHashMap<String, Object>) dataValue;
                convertToMap(kv, objectLinkedHashMap);
                result.put(key, objectLinkedHashMap);
            } else if (TypeUtil.Type.Array.name().equals(type)) {
                ArrayList<KV<String, ParamKeyValue>> dataList = (ArrayList<KV<String, ParamKeyValue>>) dataValue;
                if (dataList.size() == 0) {
                    return;
                }
                LinkedHashMap<String, Object> arrayMap = new LinkedHashMap<>();
                List<LinkedHashMap<String, Object>> list = convertArrayToMap(dataList, arrayMap);
                result.put(key, list);
            } else {
                result.put(key, dataValue);
            }
        }
    }

    /**
     * json数据 list转化为map用于text展示(递归遍历)
     *
     * @param data   数据
     * @param result 结果
     * @return
     * @author Kings
     * @date 2021/06/07
     */
    private List<LinkedHashMap<String, Object>> convertArrayToMap(ArrayList<KV<String, ParamKeyValue>> data, LinkedHashMap<String, Object> result) {
        List<LinkedHashMap<String, Object>> list = new ArrayList<>();
        for (KV<String, ParamKeyValue> kv : data) {
            kv.keySet().forEach(k -> {
                ParamKeyValue paramKeyValue = kv.get(k);
                String type = paramKeyValue.getType();
                Object value = paramKeyValue.getValue();
                if (TypeUtil.Type.Object.name().equals(type)) {
                    LinkedHashMap<String, Object> objectLinkedHashMap = new LinkedHashMap<>();
                    LinkedHashMap<String, Object> kvValue = (KV<String, Object>) value;
                    convertToMap(kvValue, objectLinkedHashMap);
                    result.put(k, objectLinkedHashMap);
                } else if (TypeUtil.Type.Array.name().equals(type)) {
                    ArrayList<KV<String, ParamKeyValue>> dataList = (ArrayList<KV<String, ParamKeyValue>>) value;
                    if (dataList.size() != 0) {
                        LinkedHashMap<String, Object> arrayDataList = new LinkedHashMap<>();
                        List<LinkedHashMap<String, Object>> l = convertArrayToMap(dataList, arrayDataList);
                        result.put(k, l);
                    }
                } else {
                    result.put(k, value);
                }
            });
            list.add(result);
        }
        return list;
    }


//    private void convertJsonArrayToNode(String key, JSONArray jsonArray, CustomNode node) {
//        jsonArray.forEach(json -> {
//            CustomNode nodeArray = new CustomNode(key, null);
//            if (json instanceof JSONObject) {
//                JSONObject valueJsonObject = (JSONObject) json;
//                node.add(convertJsonObjectToNode(nodeArray, valueJsonObject));
//            } else if (json instanceof JSONArray) {
//                JSONArray tmpJsonArray = (JSONArray) json;
//                if (tmpJsonArray.size() == 0) {
//                    return;
//                }
//                convertJsonArrayToNode(key, tmpJsonArray, nodeArray);
//                node.add(nodeArray);
//            } else {
//                node.add(new CustomNode(key, json));
//            }
//        });
//    }


    private void convertToParamKeyValueList(String prefixKey, KV<String, ParamKeyValue> data, List<ParamKeyValue> list) {
        for (Map.Entry<String, ParamKeyValue> entry : data.entrySet()) {
            String key = entry.getKey();
            ParamKeyValue value = entry.getValue();
            String type = value.getType();
            Object dataValue = value.getValue();
            if (TypeUtil.Type.Object.name().equals(type)) {
                List<ParamKeyValue> childObject = new ArrayList<>();
                convertToParamKeyValueList(prefixKey + key + ".", (KV<String, ParamKeyValue>) dataValue, childObject);
                list.addAll(childObject);
            } else if (TypeUtil.Type.Array.name().equals(type)) {
                ArrayList<KV<String, ParamKeyValue>> childList = (ArrayList<KV<String, ParamKeyValue>>) value.getValue();
                if (list.size() == 0) {
                    continue;
                }
                convertArrayToParamKeyValueList(prefixKey + key, childList, list);
            } else {
                list.add(new ParamKeyValue(prefixKey + key, dataValue, 2, type));
            }
        }
    }

    private void convertArrayToParamKeyValueList(String key, ArrayList<KV<String, ParamKeyValue>> childList, List<ParamKeyValue> list) {
        for (int i = 0; i < childList.size(); i++) {
            String arrayKey = key + "[" + i + "]";
            KV<String, ParamKeyValue> kv = childList.get(i);
            kv.forEach((k, v) -> {
                ParamKeyValue value = kv.get(k);
                Object dataValue = value.getValue();
                String type = value.getType();
                if (TypeUtil.Type.Object.name().equals(type)) {
                    convertToParamKeyValueList(arrayKey + ".", (KV<String, ParamKeyValue>) dataValue, list);
                } else if (TypeUtil.Type.Array.name().equals(type)) {
                    ArrayList<KV<String, ParamKeyValue>> childArrayList = (ArrayList<KV<String, ParamKeyValue>>) value.getValue();
                    if (childArrayList.size() != 0) {
                        convertArrayToParamKeyValueList(key + "." + arrayKey, childArrayList, list);
                    }
                } else {
                    list.add(new ParamKeyValue(arrayKey + "." + k, dataValue, 2, type));
                }
            });

        }
    }


    /**
     * json数据转树节点
     *
     * @param node 节点
     * @param data 数据
     * @return {@link CustomNode }
     * @author Kings
     * @date 2021/06/07
     */
    private CustomNode convertToNode(CustomNode node, LinkedHashMap<String, Object> data) {
        Set<String> keys = data.keySet();
        keys.forEach(key -> {
//            node.setKey(key);
            ParamKeyValue value = (ParamKeyValue) data.get(key);
            String type = value.getType();
            if (TypeUtil.Type.Object.name().equals(type)) {
                KV valueJsonObject = (KV) value.getValue();
                CustomNode customNode = new CustomNode(key, null, type);
                node.add(convertToNode(customNode, valueJsonObject));
            } else if (TypeUtil.Type.Array.name().equals(type)) {
                ArrayList<KV<String, ParamKeyValue>> list = (ArrayList<KV<String, ParamKeyValue>>) value.getValue();
                if (list.size() == 0) {
                    return;
                }
                convertArrayToNode(key, list, node);
            } else {
                node.add(new CustomNode(key, value.getValue(), type));
            }
        });
        return node;
    }


    /**
     * json数据中list转树节点
     *
     * @param key      关键
     * @param dataList 数据列表
     * @param node     节点
     * @author Kings
     * @date 2021/06/07
     */
    private void convertArrayToNode(String key, ArrayList<KV<String, ParamKeyValue>> dataList, CustomNode node) {
        CustomNode nodeArray = new CustomNode(key, null, TypeUtil.Type.Array.name());
        for (int j = 0; j < dataList.size(); j++) {
            KV<String, ParamKeyValue> kv = dataList.get(j);
            //todo add index
            CustomNode nodeArrayIndex = new CustomNode("index " + j, null, TypeUtil.Type.Array.name());

            kv.entrySet().forEach(inKv -> {
                String inKey = inKv.getKey();
                ParamKeyValue value = kv.get(inKey);
                String type = value.getType();
                if (TypeUtil.Type.Object.name().equals(type)) {
                    KV valueKvObject = (KV) value.getValue();
                    CustomNode customNode = new CustomNode(inKey, null, type);
                    nodeArrayIndex.add(convertToNode(customNode, valueKvObject));
                } else if (TypeUtil.Type.Array.name().equals(type)) {
                    ArrayList<KV<String, ParamKeyValue>> list = (ArrayList<KV<String, ParamKeyValue>>) value.getValue();
                    if (list.size() == 0) {
                        return;
                    }
                    for (int i = 0; i < list.size(); i++) {
                        CustomNode nodeArrayIn = new CustomNode(inKey, null, TypeUtil.Type.Array.name());
                        convertArrayToNode(inKey, list, nodeArrayIn);
                        nodeArrayIndex.add(nodeArrayIn);
                    }
                } else {
                    nodeArrayIndex.add(new CustomNode(inKey, value.getValue(), type));
                }
            });
            nodeArray.add(nodeArrayIndex);

        }
        node.add(nodeArray);
    }

    /**
     * 自定义节点 json树节点
     *
     * @author Kings
     * @date 2021/06/07
     * @see DefaultMutableTreeNode
     */
    private class CustomNode extends DefaultMutableTreeNode {
        private String key;
        private Object value;
        private String type;

        public CustomNode() {
        }

        public CustomNode(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        public CustomNode(String key, Object value, String type) {
            this.key = key;
            this.value = value;
            this.type = type;
        }


        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    /**
     * 创建urlEncoded table
     *
     * @return {@link JBTable }
     * @author Kings
     * @date 2021/06/02
     */
    private JBTable createPathParamKeyValueTable() {
        ColumnInfo<Object, Object>[] columns = getPathColumnInfo();
        if (pathParamsKeyValueList == null) {
            pathParamsKeyValueList = new ArrayList<>();
        }
        ListTableModel<ParamKeyValue> model = new ListTableModel<>(columns, pathParamsKeyValueList);
        JBTable table = new JBTable(model) {

            @Override
            public @NotNull Component prepareRenderer(@NotNull TableCellRenderer renderer, int row, int column) {
                if (column == 0) {
                    ParamKeyValue paramKeyValue = pathParamsKeyValueList.get(row);
                    String type = paramKeyValue.getType();
                    ComboBox typeComboBox = getNormalTypeComboBox(type);
                    return typeComboBox;
                }
                if (column == 1) {
                    ParamKeyValue paramKeyValue = pathParamsKeyValueList.get(row);
                    String type = paramKeyValue.getType();
                    String key = paramKeyValue.getKey();
                    JLabel label = new JLabel(key, JLabel.LEFT);
                    label.setFont(label.getFont().deriveFont(Font.ITALIC));
                    label.setHorizontalAlignment(JLabel.LEFT);
                    label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                    label.setIconTextGap(5);
                    return label;
                }
                return super.prepareRenderer(renderer, row, column);
            }


            @Override
            public Object getValueAt(int row, int column) {
                if (pathParamsKeyValueList.isEmpty()) {
                    return StringUtils.EMPTY;
                }
                ParamKeyValue keyValue = pathParamsKeyValueList.get(row);
                if (keyValue == null) {
                    return StringUtils.EMPTY;
                }
                if (column == 0) {
                    return keyValue.getType();
                } else if (column == 1) {
                    return keyValue.getKey();
                } else {
                    return keyValue.getValue();
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }

            @Override
            public void setValueAt(Object aValue, int row, int column) {
                if (column == 0) {
                    ParamKeyValue paramKeyValue = pathParamsKeyValueList.get(row);
                    paramKeyValue.setType(aValue.toString());
                }
                if (column == 1) {
                    boolean changeFlag = false;
                    ParamKeyValue paramKeyValue = pathParamsKeyValueList.get(row);
                    String value = aValue.toString();
                    if (!paramKeyValue.getValue().equals(value)) {
                        changeFlag = true;
                    }
                    paramKeyValue.setKey(aValue.toString());
                    if (changeFlag) {
                        changeUrl();
                    }
                }
                if (column == 2) {
                    boolean changeFlag = false;
                    String value = aValue.toString();
                    ParamKeyValue paramKeyValue = pathParamsKeyValueList.get(row);
                    if (!paramKeyValue.getValue().equals(value)) {
                        changeFlag = true;
                    }
                    paramKeyValue.setValue(value);
                    if (changeFlag) {
                        changeUrl();
                    }
                }

            }
        };
        table.setVisible(true);
        return table;
    }

    /**
     * 创建urlEncoded table
     *
     * @return {@link JBTable }
     * @author Kings
     * @date 2021/06/02
     */
    public JBTable createUrlEncodedKeyValueTable() {
        ColumnInfo<Object, Object>[] columns = getPathColumnInfo();
        if (urlEncodedKeyValueList == null) {
            urlEncodedKeyValueList = new ArrayList<>();
        }
        ListTableModel<ParamKeyValue> model = new ListTableModel<>(columns, urlEncodedKeyValueList);
        JBTable table = new JBTable(model) {

            @Override
            public @NotNull Component prepareRenderer(@NotNull TableCellRenderer renderer, int row, int column) {
                if (column == 0) {
                    ParamKeyValue paramKeyValue = urlEncodedKeyValueList.get(row);
                    String type = paramKeyValue.getType();
                    ComboBox typeComboBox = getNormalTypeComboBox(type);
                    return typeComboBox;
                }
                if (column == 1) {
                    ParamKeyValue paramKeyValue = urlEncodedKeyValueList.get(row);
                    String key = paramKeyValue.getKey();
                    JLabel label = new JLabel(key, JLabel.LEFT);
                    label.setFont(label.getFont().deriveFont(Font.ITALIC));
                    label.setHorizontalAlignment(JLabel.LEFT);
                    label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                    return label;
                }
                return super.prepareRenderer(renderer, row, column);
            }

            @Override
            public Object getValueAt(int row, int column) {
                if (urlEncodedKeyValueList.isEmpty()) {
                    return StringUtils.EMPTY;
                }
                ParamKeyValue keyValue = urlEncodedKeyValueList.get(row);
                if (keyValue == null) {
                    return StringUtils.EMPTY;
                }
                if (column == 0) {
                    return keyValue.getType();
                } else if (column == 1) {
                    return keyValue.getKey();
                } else {
                    return keyValue.getValue();
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }

            @Override
            public void setValueAt(Object aValue, int row, int column) {
                if (column == 0) {
                    ParamKeyValue paramKeyValue = urlEncodedKeyValueList.get(row);
                    paramKeyValue.setType(aValue.toString());
                }
                if (column == 1) {
                    ParamKeyValue paramKeyValue = urlEncodedKeyValueList.get(row);
                    paramKeyValue.setKey(aValue.toString());
                    String value = aValue.toString();
                    if (!paramKeyValue.getValue().equals(value)) {
                        urlEncodedParamChangeFlag.set(true);
                    }
                }
                if (column == 2) {
                    String value = aValue.toString();
                    ParamKeyValue paramKeyValue = urlEncodedKeyValueList.get(row);
                    if (!paramKeyValue.getValue().equals(value)) {
                        urlEncodedParamChangeFlag.set(true);
                    }
                    paramKeyValue.setValue(value);
                }
            }
        };
        table.setVisible(true);
        return table;
    }

    /**
     * 创建urlParams table
     *
     * @return {@link JBTable }
     * @author Kings
     * @date 2021/06/02
     */
    public JBTable createUrlParamsKeyValueTable() {
        ColumnInfo<Object, Object>[] columns = getPathColumnInfo();
        if (urlParamsKeyValueList == null) {
            urlParamsKeyValueList = new ArrayList<>();
        }
        ListTableModel<ParamKeyValue> model = new ListTableModel<>(columns, urlParamsKeyValueList);
        JBTable table = new JBTable(model) {
            @Override
            public @NotNull Component prepareRenderer(@NotNull TableCellRenderer renderer, int row, int column) {
                if (column == 0) {
                    ParamKeyValue paramKeyValue = urlParamsKeyValueList.get(row);
                    String type = paramKeyValue.getType();
                    ComboBox typeComboBox = getNormalTypeComboBox(type);
                    return typeComboBox;
                }
                if (column == 1) {
                    ParamKeyValue paramKeyValue = urlParamsKeyValueList.get(row);
                    String key = paramKeyValue.getKey();
                    JLabel label = new JLabel(key, JLabel.LEFT);
                    label.setFont(label.getFont().deriveFont(Font.ITALIC));
                    label.setHorizontalAlignment(JLabel.LEFT);
                    label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                    return label;
                }
                return super.prepareRenderer(renderer, row, column);
            }

            @Override
            public Object getValueAt(int row, int column) {
                if (urlParamsKeyValueList.isEmpty()) {
                    return StringUtils.EMPTY;
                }
                ParamKeyValue keyValue = urlParamsKeyValueList.get(row);
                if (keyValue == null) {
                    return StringUtils.EMPTY;
                }
                if (column == 0) {
                    return keyValue.getType();
                } else if (column == 1) {
                    return keyValue.getKey();
                } else {
                    return keyValue.getValue();
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                //默认只允许修改value不允许修改key
                return true;
            }

            @Override
            public void setValueAt(Object aValue, int row, int column) {
                if (column == 0) {
                    ParamKeyValue paramKeyValue = urlParamsKeyValueList.get(row);
                    paramKeyValue.setType(aValue.toString());
                }
                if (column == 1) {
                    ParamKeyValue paramKeyValue = urlParamsKeyValueList.get(row);
                    paramKeyValue.setKey(aValue.toString());
                    String value = aValue.toString();
                    if (!paramKeyValue.getValue().equals(value)) {
                        urlParamsChangeFlag.set(true);
                    }
                }
                if (column == 2) {
                    String value = aValue.toString();
                    ParamKeyValue paramKeyValue = urlParamsKeyValueList.get(row);
                    if (!paramKeyValue.getValue().equals(value)) {
                        urlParamsChangeFlag.set(true);
                    }
                    paramKeyValue.setValue(value);
                }
            }

        };
        table.setVisible(true);
        return table;
    }

    /*****getter setter*****/

    public JComponent getContent() {
        return panel;
    }

    public JPanel getPanel() {
        return panel;
    }

}
