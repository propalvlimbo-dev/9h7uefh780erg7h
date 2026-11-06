package sg.gui.manager.now;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;

public final class AccountManagerPanel {
    private final AccountStore accountStore = AccountStore.getAccountStore();
    private TypeOption nested0482 = TypeOption.NONE;
    private String strVal = "";
    private String strVal2 = "";
    private boolean flag;
    private final NicknameGenerator nicknameGenerator = new NicknameGenerator();
    private final HashMap<String, AnimatedValue> map = new HashMap<>();
    private float floatVal;
    private float floatVal2;
    private float floatVal3;
    private long timeMillis = System.nanoTime() / 1000000L;
    private long timeMillis2;
    private long __;
    private String strVal3;
    private Runnable runnable2 = () -> {};
    private float floatVal4;
    private float floatVal5;
    private float floatVal6 = 1.0F;
    private final ArrayList<DataRecord2> list = new ArrayList<>();
    private ArrayList<AccountEntry> list2 = new ArrayList<>();

    public void renderElement(GuiGraphicsExtractor drawContext, float local5260_2, float local5260_3, float local5260_4, float local5260_5, float local5260_6) {
        this.floatVal6 = local5260_3;
        float local5260_7 = Math.min(0.05F, (float)(System.nanoTime() / 1000000L - this.timeMillis) / 1000.0F);
        this.timeMillis = System.nanoTime() / 1000000L;
        this.list.clear();
        AccountEntry accountEntry = this.accountStore.getAccountEntry();
        String local5260_9 = accountEntry == null ? null : accountEntry.formatText4();
        if (local5260_9 != null && !local5260_9.equals(this.strVal3)) {
            this.strVal3 = local5260_9;
            this.__ = System.currentTimeMillis();
        }

        float local5260_10 = local5260_2 + 51.5F * local5260_3;
        this.processData4(local5260_10, local5260_4, local5260_5, local5260_6);
        this.processData5(local5260_10, local5260_4);
        this.renderElement2(drawContext, local5260_10, local5260_4, local5260_5, local5260_6, local5260_7);
        this.processData3(local5260_10, local5260_4, local5260_5, local5260_6, accountEntry);
        this.processData(local5260_10, local5260_4, local5260_5, local5260_6);
    }

    private float computeFloat(int local5263_1) {
        return UiTheme.computeFloat2(UiTheme.computeFloat3(this.timeMillis2, 40L + local5263_1 * 55L, 420L));
    }

    private void processData(float local5264_1, float local5264_2, float local5264_3, float local5264_4) {
        float local5264_5 = this.computeFloat(4);
        Render2D.processData46(local5264_5);
        float local5264_6 = local5264_1 - (1.0F - local5264_5) * 14.0F * this.floatVal6;
        float local5264_7 = local5264_2 + 411.5F * this.floatVal6;
        float local5264_8 = this.computeFloat2(
                "altGenerate", UiTheme.matchesCriteria(local5264_3, local5264_4, local5264_6, local5264_7, 109.5F * this.floatVal6, 19.5F * this.floatVal6)
        );
        Render2D.processData51(
                local5264_6,
                local5264_7,
                109.5F * this.floatVal6,
                19.5F * this.floatVal6,
                6.0F * this.floatVal6,
                UiTheme.computeInt(UiTheme.intVal, 0.031F + 0.05F * local5264_8)
        );
        Render2D.processData86(
                local5264_6,
                local5264_7,
                109.5F * this.floatVal6,
                19.5F * this.floatVal6,
                6.0F * this.floatVal6,
                0.5F,
                UiTheme.computeInt(UiTheme.intVal, 0.078F + 0.09F * local5264_8)
        );
        float diceSize = 6.5F * this.floatVal6;
        float diceY = local5264_7 + (19.5F * this.floatVal6 - diceSize) / 2.0F;
        UiTheme.processData3(
                "assets/deluxe/textures/gui/mainmenu/icons/dice.png",
                local5264_6 + 8.5F * this.floatVal6,
                diceY,
                diceSize,
                diceSize,
                UiTheme.computeInt2(UiTheme.intVal28, UiTheme.intVal, local5264_8)
        );
        FontText.processData3(
                FontFace.SEMI,
                "Сгенерировать аккаунт",
                local5264_6 + 18.5F * this.floatVal6,
                local5264_7 + (19.5F * this.floatVal6 - 7.0F * this.floatVal6) / 2.0F,
                7.0F * this.floatVal6,
                UiTheme.computeInt2(UiTheme.intVal28, UiTheme.intVal, local5264_8)
        );
        this.list.add(
                new DataRecord2(
                        local5264_6,
                        local5264_7,
                        109.5F * this.floatVal6,
                        19.5F * this.floatVal6,
                        false,
                        () -> this.accountStore.processData5(this.accountStore.getAccountEntry3(this.nicknameGenerator.formatText11()))
                )
        );
        float local5264_9 = local5264_6 + 117.0F * this.floatVal6;
        float local5264_10 = this.computeFloat2(
                "altCreate", UiTheme.matchesCriteria(local5264_3, local5264_4, local5264_9, local5264_7, 109.5F * this.floatVal6, 19.5F * this.floatVal6)
        );
        float local5264_11 = this.computeFloat2("altCreateActive", this.nested0482 == TypeOption.CREATE);
        Render2D.processData51(
                local5264_9,
                local5264_7,
                109.5F * this.floatVal6,
                19.5F * this.floatVal6,
                6.0F * this.floatVal6,
                UiTheme.computeInt2(
                        UiTheme.computeInt(UiTheme.intVal, 0.031F + 0.05F * local5264_10),
                        UiTheme.computeInt(UiTheme.intVal7, 0.85F),
                        local5264_11
                )
        );
        Render2D.processData86(
                local5264_9,
                local5264_7,
                109.5F * this.floatVal6,
                19.5F * this.floatVal6,
                6.0F * this.floatVal6,
                0.5F,
                UiTheme.computeInt2(
                        UiTheme.computeInt(UiTheme.intVal, 0.078F + 0.09F * local5264_10), UiTheme.intVal34, local5264_11
                )
        );
        float plusSize = 6.5F * this.floatVal6;
        float plusY = local5264_7 + (19.5F * this.floatVal6 - plusSize) / 2.0F;
        UiTheme.processData3(
                "assets/deluxe/textures/gui/mainmenu/icons/plus.png",
                local5264_9 + 18.5F * this.floatVal6,
                plusY,
                plusSize,
                plusSize,
                UiTheme.computeInt2(UiTheme.intVal28, UiTheme.intVal, Math.max(local5264_10, local5264_11))
        );
        FontText.processData3(
                FontFace.SEMI,
                this.nested0482 == TypeOption.CREATE ? "Подтвердить" : "Создать аккаунт",
                local5264_9 + 28.5F * this.floatVal6,
                local5264_7 + (19.5F * this.floatVal6 - 7.0F * this.floatVal6) / 2.0F,
                7.0F * this.floatVal6,
                UiTheme.computeInt2(UiTheme.intVal28, UiTheme.intVal, Math.max(local5264_10, local5264_11))
        );
        this.list.add(new DataRecord2(local5264_9, local5264_7, 109.5F * this.floatVal6, 19.5F * this.floatVal6, false, () -> {
            if (this.nested0482 != TypeOption.CREATE) {
                this.nested0482 = TypeOption.CREATE;
                this.strVal2 = "";
                this.flag = false;
            } else {
                this.updateState2();
            }
        }));
        Render2D.updateState();
    }

    private void renderElement2(GuiGraphicsExtractor drawContext, float local5265_2, float local5265_3, float local5265_4, float local5265_5, float local5265_6) {
        float local5265_7 = this.computeFloat(2);
        Render2D.processData46(local5265_7);
        float local5265_8 = local5265_2 - (1.0F - local5265_7) * 14.0F * this.floatVal6;
        this.floatVal4 = local5265_3 + 223.5F * this.floatVal6;
        this.floatVal5 = this.floatVal4 + 138.5F * this.floatVal6;
        ArrayList<AccountEntry> local5265_9 = this.accountStore.getArrayList2(this.strVal);
        this.list2 = local5265_9;
        this.floatVal3 = Math.max(0.0F, local5265_9.size() * 22.5F - 4.5F - 138.5F);
        this.floatVal = computeFloat5(this.floatVal, 0.0F, this.floatVal3);
        this.floatVal2 = computeFloat4(this.floatVal2, this.floatVal, local5265_6);
        Render2D.renderElement2(drawContext, local5265_8, this.floatVal4, 227.0F * this.floatVal6, 138.5F * this.floatVal6);
        if (local5265_9.isEmpty()) {
            String local5265_10 = this.strVal.isEmpty() ? "Аккаунтов пока нет" : "Ничего не найдено";
            FontText.processData3(
                    FontFace.SEMI,
                    local5265_10,
                    local5265_8 + (227.0F * this.floatVal6 - FontText.computeFloat(FontFace.SEMI, local5265_10, 7.0F * this.floatVal6)) / 2.0F,
                    this.floatVal4 + 64.0F * this.floatVal6,
                    7.0F * this.floatVal6,
                    UiTheme.intVal30
            );
        }

        float local5265_10 = this.floatVal4 - this.computeFloat3(this.floatVal2) * this.floatVal6;
        int local5265_11 = 0;

        for (AccountEntry accountEntry : local5265_9) {
            if (local5265_10 + 16.5F * this.floatVal6 >= this.floatVal4 - 6.0F * this.floatVal6 && local5265_10 <= this.floatVal5 + 6.0F * this.floatVal6) {
                this.processData6(accountEntry, local5265_8, local5265_10, local5265_11, local5265_4, local5265_5);
            }

            local5265_10 += 22.5F * this.floatVal6;
            local5265_11++;
        }

        Render2D.renderElement(drawContext);
        if (this.floatVal3 > 0.5F) {
            float local5265_12 = Math.max(16.619999F, 138.5F * (138.5F / (local5265_9.size() * 22.5F - 4.5F))) * this.floatVal6;
            Render2D.processData51(
                    local5265_8 + 226.5F * this.floatVal6,
                    this.floatVal4,
                    0.5F * this.floatVal6,
                    138.5F * this.floatVal6,
                    0.25F * this.floatVal6,
                    UiTheme.computeInt(UiTheme.intVal, 0.05F)
            );
            Render2D.processData51(
                    local5265_8 + 226.5F * this.floatVal6,
                    this.floatVal4 + this.floatVal2 / this.floatVal3 * (138.5F * this.floatVal6 - local5265_12),
                    0.5F * this.floatVal6,
                    local5265_12,
                    0.25F * this.floatVal6,
                    UiTheme.intVal31
            );
        }

        Render2D.updateState();
    }

    private float computeFloat2(String local5267_1, boolean local5267_2) {
        AnimatedValue animatedValue = this.map.computeIfAbsent(local5267_1, k -> new AnimatedValue(140L));
        animatedValue.processData(local5267_2 ? 1.0 : 0.0);
        return (float)animatedValue.computeDouble();
    }

    private float computeFloat3(float local5268_1) {
        return Math.round(local5268_1);
    }

    public boolean matchesCriteria(float local5269_1, float local5269_2) {
        boolean local5269_3 = this.nested0482 != TypeOption.NONE;
        this.nested0482 = this.nested0482 == TypeOption.SEARCH ? TypeOption.NONE : this.nested0482;

        for (int index = this.list.size() - 1; index >= 0; index--) {
            DataRecord2 local5269_5 = this.list.get(index);
            if ((!local5269_5.clipped() || !(local5269_2 < this.floatVal4) && !(local5269_2 > this.floatVal5))
                    && UiTheme.matchesCriteria(local5269_1, local5269_2, local5269_5.x(), local5269_5.y(), local5269_5.w(), local5269_5.h())) {
                local5269_5.action().run();
                return true;
            }
        }

        if (local5269_3) {
            this.nested0482 = TypeOption.NONE;
            this.flag = false;
            return true;
        } else {
            return false;
        }
    }

    public void updateState() {
        this.nested0482 = TypeOption.NONE;
        this.flag = false;
    }

    public boolean matchesCriteria2(float local5271_1, float local5271_2, float local5271_3) {
        if (local5271_2 >= this.floatVal4 - 30.0F * this.floatVal6 && local5271_2 <= this.floatVal5 + 10.0F * this.floatVal6) {
            this.floatVal = computeFloat5(this.floatVal - local5271_3 * 22.5F, 0.0F, this.floatVal3);
            return true;
        } else {
            return false;
        }
    }

    private static float computeFloat4(float local5272_0, float local5272_1, float local5272_2) {
        local5272_0 += (local5272_1 - local5272_0) * (1.0F - (float)Math.exp(-local5272_2 * 22.0F));
        return Math.abs(local5272_1 - local5272_0) < 0.1F ? local5272_1 : local5272_0;
    }

    public boolean matchesCriteria3(int local5274_1) {
        if (this.nested0482 == TypeOption.NONE) {
            return false;
        }

        boolean local5274_2 = this.nested0482 == TypeOption.SEARCH;
        long window = Minecraft.getInstance().getWindow().handle();
        if (GLFW.glfwGetKey(window, 341) == 1 || GLFW.glfwGetKey(window, 345) == 1) {
            if (local5274_1 == 65) {
                this.flag = true;
                return true;
            }

            if (local5274_1 == 67) {
                String var10001 = local5274_2 ? this.strVal : this.strVal2;
                Minecraft.getInstance().keyboardHandler.setClipboard(var10001);
                return true;
            }

            if (local5274_1 == 86) {
                String local5274_3 = Minecraft.getInstance().keyboardHandler.getClipboard().replaceAll("[\\r\\n\\t]", "");
                if (this.flag) {
                    if (local5274_2) {
                        this.strVal = "";
                    } else {
                        this.strVal2 = "";
                    }

                    this.flag = false;
                }

                if (local5274_2) {
                    this.strVal = (this.strVal + local5274_3).length() > 24
                            ? (this.strVal + local5274_3).substring(0, 24)
                            : this.strVal + local5274_3;
                } else {
                    this.strVal2 = (this.strVal2 + local5274_3).length() > 16
                            ? (this.strVal2 + local5274_3).substring(0, 16)
                            : this.strVal2 + local5274_3;
                }

                return true;
            }
        }

        if (local5274_1 == 259) {
            if (this.flag) {
                if (local5274_2) {
                    this.strVal = "";
                } else {
                    this.strVal2 = "";
                }

                this.flag = false;
                return true;
            } else {
                if (local5274_2 && !this.strVal.isEmpty()) {
                    this.strVal = this.strVal.substring(0, this.strVal.length() - 1);
                } else if (!local5274_2 && !this.strVal2.isEmpty()) {
                    this.strVal2 = this.strVal2.substring(0, this.strVal2.length() - 1);
                }

                return true;
            }
        } else if (local5274_1 != 257 && local5274_1 != 335) {
            if (local5274_1 == 256) {
                this.nested0482 = TypeOption.NONE;
                this.flag = false;
                return true;
            } else {
                return false;
            }
        } else {
            if (local5274_2) {
                this.nested0482 = TypeOption.NONE;
                this.flag = false;
            } else {
                this.updateState2();
            }

            return true;
        }
    }

    private static float computeFloat5(float local5276_0, float local5276_1, float local5276_2) {
        return Math.max(local5276_1, Math.min(local5276_2, local5276_0));
    }

    public void processData2(Runnable runnable) {
        this.accountStore.refresh();
        this.timeMillis2 = System.currentTimeMillis();
        this.runnable2 = runnable;
        this.nested0482 = TypeOption.NONE;
        this.flag = false;
        this.strVal2 = "";
        this.__ = 0L;
        this.strVal3 = this.accountStore.getAccountEntry() == null ? null : this.accountStore.getAccountEntry().formatText4();
    }

    private void processData3(float local5279_1, float local5279_2, float local5279_3, float local5279_4, AccountEntry accountEntry) {
        float local5279_6 = this.computeFloat(3);
        Render2D.processData46(local5279_6);
        float local5279_7 = local5279_1 - (1.0F - local5279_6) * 14.0F * this.floatVal6;
        float local5279_8 = local5279_2 + 372.5F * this.floatVal6;
        if (accountEntry == null) {
            Render2D.processData51(
                    local5279_7,
                    local5279_8,
                    226.5F * this.floatVal6,
                    32.0F * this.floatVal6,
                    6.0F * this.floatVal6,
                    UiTheme.computeInt(UiTheme.intVal, 0.031F)
            );
            Render2D.processData86(
                    local5279_7, local5279_8, 226.5F * this.floatVal6, 32.0F * this.floatVal6, 6.0F * this.floatVal6, 0.5F, UiTheme.intVal33
            );
            String local5279_9 = "Выберите аккаунт из списка";
            FontText.processData3(
                    FontFace.SEMI,
                    local5279_9,
                    local5279_7 + (226.5F * this.floatVal6 - FontText.computeFloat(FontFace.SEMI, local5279_9, 7.0F * this.floatVal6)) / 2.0F,
                    local5279_8 + 12.5F * this.floatVal6,
                    7.0F * this.floatVal6,
                    UiTheme.intVal30
            );
            Render2D.updateState();
        } else {
            float local5279_9 = this.__ == 0L ? 1.0F : UiTheme.computeFloat2(UiTheme.computeFloat3(this.__, 0L, 340L));
            Render2D.processData51(
                    local5279_7, local5279_8, 226.5F * this.floatVal6, 32.0F * this.floatVal6, 6.0F * this.floatVal6, UiTheme.intVal7
            );
            Render2D.processData86(
                    local5279_7, local5279_8, 226.5F * this.floatVal6, 32.0F * this.floatVal6, 6.0F * this.floatVal6, 0.5F, UiTheme.intVal34
            );
            Render2D.processData46(local5279_9);
            float local5279_10 = (1.0F - local5279_9) * 4.0F * this.floatVal6;
            UiTheme.processData3(
                    "assets/deluxe/textures/gui/mainmenu/icons/user.png",
                    local5279_7 + 38.0F * this.floatVal6,
                    local5279_8 + 7.5F * this.floatVal6 + local5279_10,
                    5.5F * this.floatVal6,
                    5.5F * this.floatVal6,
                    UiTheme.computeInt(UiTheme.intVal10, 0.7F)
            );
            FontText.processData3(
                    FontFace.SEMI,
                    "Выбранный аккаунт",
                    local5279_7 + 45.0F * this.floatVal6,
                    local5279_8 + 7.0F * this.floatVal6 + local5279_10,
                    6.0F * this.floatVal6,
                    UiTheme.computeInt(UiTheme.intVal10, 0.7F)
            );
            FontText.processData3(
                    FontFace.SEMI_BOLD,
                    accountEntry.formatText4(),
                    local5279_7 + 38.0F * this.floatVal6,
                    local5279_8 + 16.0F * this.floatVal6 + local5279_10,
                    7.0F * this.floatVal6,
                    UiTheme.intVal10
            );
            Render2D.updateState();
            float local5279_11 = this.computeFloat2(
                    "altRename",
                    UiTheme.matchesCriteria(
                            local5279_3,
                            local5279_4,
                            local5279_7 + 185.0F * this.floatVal6,
                            local5279_8 + 10.5F * this.floatVal6,
                            11.0F * this.floatVal6,
                            11.0F * this.floatVal6
                    )
            );
            UiTheme.processData3(
                    "assets/deluxe/textures/gui/mainmenu/icons/rename.png",
                    local5279_7 + 187.0F * this.floatVal6,
                    local5279_8 + 12.5F * this.floatVal6,
                    7.0F * this.floatVal6,
                    7.0F * this.floatVal6,
                    UiTheme.computeInt(UiTheme.intVal10, 0.72F + 0.28F * local5279_11)
            );
            this.list.add(
                    new DataRecord2(
                            local5279_7 + 185.0F * this.floatVal6,
                            local5279_8 + 10.5F * this.floatVal6,
                            11.0F * this.floatVal6,
                            11.0F * this.floatVal6,
                            false,
                            () -> {
                                this.nested0482 = TypeOption.RENAME;
                                this.strVal2 = accountEntry.formatText4();
                                this.flag = true;
                            }
                    )
            );
            float local5279_12 = this.computeFloat2(
                    "altDelete",
                    UiTheme.matchesCriteria(
                            local5279_3,
                            local5279_4,
                            local5279_7 + 203.0F * this.floatVal6,
                            local5279_8 + 10.5F * this.floatVal6,
                            11.0F * this.floatVal6,
                            11.0F * this.floatVal6
                    )
            );
            UiTheme.processData3(
                    "assets/deluxe/textures/gui/mainmenu/icons/delete.png",
                    local5279_7 + 205.0F * this.floatVal6,
                    local5279_8 + 12.5F * this.floatVal6,
                    7.0F * this.floatVal6,
                    7.0F * this.floatVal6,
                    UiTheme.computeInt2(UiTheme.computeInt(UiTheme.intVal10, 0.72F), UiTheme.intVal35, local5279_12)
            );
            this.list.add(
                    new DataRecord2(
                            local5279_7 + 203.0F * this.floatVal6,
                            local5279_8 + 10.5F * this.floatVal6,
                            11.0F * this.floatVal6,
                            11.0F * this.floatVal6,
                            false,
                            () -> this.accountStore.processData3(accountEntry)
                    )
            );
            Render2D.updateState();
        }
    }

    private void processData4(float local5280_1, float local5280_2, float local5280_3, float local5280_4) {
        float local5280_5 = this.computeFloat(0);
        Render2D.processData46(local5280_5);
        float local5280_6 = local5280_1 - (1.0F - local5280_5) * 14.0F * this.floatVal6;
        float local5280_7 = this.computeFloat2(
                "altBack",
                UiTheme.matchesCriteria(
                        local5280_3,
                        local5280_4,
                        local5280_6 - 4.0F * this.floatVal6,
                        local5280_2 + 105.0F * this.floatVal6,
                        18.0F * this.floatVal6,
                        18.0F * this.floatVal6
                )
        );
        UiTheme.processData3(
                "assets/deluxe/textures/gui/mainmenu/icons/back.png",
                local5280_6 + 0.25F * this.floatVal6 - local5280_7 * 1.5F * this.floatVal6,
                local5280_2 + 109.25F * this.floatVal6,
                9.5F * this.floatVal6,
                9.5F * this.floatVal6,
                UiTheme.computeInt2(UiTheme.intVal29, UiTheme.intVal, local5280_7)
        );
        this.list.add(
                new DataRecord2(
                        local5280_6 - 4.0F * this.floatVal6,
                        local5280_2 + 105.0F * this.floatVal6,
                        18.0F * this.floatVal6,
                        18.0F * this.floatVal6,
                        false,
                        this.runnable2
                )
        );
        FontText.processData3(
                FontFace.SEMI,
                "Rose  /  Главная  /  Аккаунты",
                local5280_6 + 15.5F * this.floatVal6,
                local5280_2 + 110.5F * this.floatVal6,
                6.5F * this.floatVal6,
                UiTheme.intVal29
        );
        FontText.processData3(
                FontFace.SEMI,
                "Управление аккаунтами.",
                local5280_6,
                local5280_2 + 124.5F * this.floatVal6,
                20.0F * this.floatVal6,
                UiTheme.intVal
        );
        FontText.processData3(
                FontFace.SEMI,
                "Создавайте и выбирайте игровые профили",
                local5280_6,
                local5280_2 + 159.5F * this.floatVal6,
                7.5F * this.floatVal6,
                UiTheme.intVal29
        );
        Render2D.updateState();
    }

    private void processData5(float local5281_1, float local5281_2) {
        float local5281_3 = this.computeFloat(1);
        Render2D.processData46(local5281_3);
        float local5281_4 = local5281_1 - (1.0F - local5281_3) * 14.0F * this.floatVal6;
        float local5281_5 = local5281_2 + 184.5F * this.floatVal6;
        boolean local5281_6 = this.nested0482 != TypeOption.NONE;
        float local5281_7 = this.computeFloat2("altFieldFocus", local5281_6);
        Render2D.processData51(
                local5281_4,
                local5281_5,
                226.5F * this.floatVal6,
                19.5F * this.floatVal6,
                6.0F * this.floatVal6,
                UiTheme.computeInt(UiTheme.intVal, 0.031F)
        );
        Render2D.processData86(
                local5281_4,
                local5281_5,
                226.5F * this.floatVal6,
                19.5F * this.floatVal6,
                6.0F * this.floatVal6,
                0.5F,
                UiTheme.computeInt2(UiTheme.intVal33, UiTheme.computeInt(UiTheme.intVal34, 0.8F), local5281_7)
        );
        float searchIconSize = 6.5F * this.floatVal6;
        float searchIconY = local5281_5 + (19.5F * this.floatVal6 - searchIconSize) / 2.0F;
        UiTheme.processData3(
                this.nested0482 == TypeOption.CREATE
                        ? "assets/deluxe/textures/gui/mainmenu/icons/plus.png"
                        : (
                        this.nested0482 == TypeOption.RENAME
                                ? "assets/deluxe/textures/gui/mainmenu/icons/rename.png"
                                : "assets/deluxe/textures/gui/mainmenu/icons/search.png"
                ),
                local5281_4 + 8.5F * this.floatVal6,
                searchIconY,
                searchIconSize,
                searchIconSize,
                UiTheme.computeInt2(UiTheme.intVal30, UiTheme.computeInt(UiTheme.intVal34, 0.95F), local5281_7)
        );
        String local5281_8 = this.nested0482 != TypeOption.SEARCH && this.nested0482 != TypeOption.NONE
                ? this.strVal2
                : this.strVal;
        String local5281_9 = this.nested0482 == TypeOption.CREATE
                ? "Введите ник нового аккаунта..."
                : (this.nested0482 == TypeOption.RENAME ? "Новый ник аккаунта..." : "Найти аккаунт...");
        if (local5281_6 && this.flag && !local5281_8.isEmpty()) {
            Render2D.processData51(
                    local5281_4 + 19.0F * this.floatVal6,
                    local5281_5 + 5.5F * this.floatVal6,
                    FontText.computeFloat(FontFace.SEMI, local5281_8, 7.0F * this.floatVal6) + 1.0F * this.floatVal6,
                    9.0F * this.floatVal6,
                    2.0F * this.floatVal6,
                    UiTheme.computeInt(UiTheme.intVal7, 0.3F)
            );
        }

        FontText.processData3(
                FontFace.SEMI,
                (local5281_8.isEmpty() && !local5281_6 ? local5281_9 : local5281_8) + (local5281_6 && System.currentTimeMillis() % 1000L < 500L ? "|" : ""),
                local5281_4 + 19.0F * this.floatVal6,
                local5281_5 + 6.25F * this.floatVal6,
                7.0F * this.floatVal6,
                local5281_8.isEmpty() ? UiTheme.intVal30 : UiTheme.intVal
        );
        if (local5281_8.isEmpty() && local5281_6) {
            FontText.processData3(
                    FontFace.SEMI,
                    local5281_9,
                    local5281_4
                            + 19.0F * this.floatVal6
                            + FontText.computeFloat(FontFace.SEMI, "|", 7.0F * this.floatVal6)
                            + 1.5F * this.floatVal6,
                    local5281_5 + 6.25F * this.floatVal6,
                    7.0F * this.floatVal6,
                    UiTheme.computeInt(UiTheme.intVal, 0.16F)
            );
        }

        this.list.add(new DataRecord2(local5281_4, local5281_5, 226.5F * this.floatVal6, 19.5F * this.floatVal6, false, () -> {
            if (this.nested0482 == TypeOption.NONE) {
                this.nested0482 = TypeOption.SEARCH;
            }
        }));
        float local5281_10 = local5281_2 + 210.0F * this.floatVal6;
        FontText.processData3(FontFace.SEMI, "Ваши аккаунты", local5281_4, local5281_10, 6.0F * this.floatVal6, UiTheme.intVal29);
        Render2D.processData51(
                local5281_4 + 51.5F * this.floatVal6,
                local5281_10 + 3.5F * this.floatVal6,
                175.0F * this.floatVal6,
                0.5F * this.floatVal6,
                0.25F * this.floatVal6,
                UiTheme.intVal31
        );
        Render2D.updateState();
    }

    private void updateState2() {
        if (this.nested0482 == TypeOption.CREATE && !this.strVal2.isBlank()) {
            this.accountStore.processData5(this.accountStore.getAccountEntry3(this.strVal2));
        }

        if (this.nested0482 == TypeOption.RENAME && !this.strVal2.isBlank()) {
            this.accountStore.processData(this.accountStore.getAccountEntry(), this.strVal2);
        }

        this.strVal2 = "";
        this.flag = false;
        this.nested0482 = TypeOption.NONE;
    }

    private void processData6(AccountEntry accountEntry, float local5284_2, float local5284_3, int local5284_4, float local5284_5, float local5284_6) {
        float local5284_7 = UiTheme.computeFloat2(UiTheme.computeFloat3(this.timeMillis2, 200L + Math.min(local5284_4, 8) * 45L, 380L));
        float local5284_8 = UiTheme.computeFloat2(Math.min(1.0F, (float)(System.currentTimeMillis() - accountEntry.computeLong()) / 260.0F));
        Render2D.processData46(local5284_7 * local5284_8);
        float local5284_9 = local5284_2 + (1.0F - local5284_7) * 8.0F * this.floatVal6 + (1.0F - local5284_8) * 8.0F * this.floatVal6;
        float local5284_10 = this.computeFloat2("altRowActive" + accountEntry.formatText4(), this.accountStore.matchesCriteria(accountEntry));
        float local5284_11 = this.computeFloat2(
                "altRow" + accountEntry.formatText4(),
                local5284_10 < 0.5F
                        && UiTheme.matchesCriteria(local5284_5, local5284_6, local5284_9, local5284_3, 218.5F * this.floatVal6, 16.5F * this.floatVal6)
                        && local5284_6 >= this.floatVal4
                        && local5284_6 <= this.floatVal5
        );
        Render2D.processData51(
                local5284_9,
                local5284_3,
                218.5F * this.floatVal6,
                16.5F * this.floatVal6,
                5.0F * this.floatVal6,
                UiTheme.computeInt2(
                        UiTheme.computeInt(UiTheme.intVal, 0.031F + 0.045F * local5284_11), UiTheme.intVal7, local5284_10
                )
        );
        Render2D.processData86(
                local5284_9,
                local5284_3,
                218.5F * this.floatVal6,
                16.5F * this.floatVal6,
                5.0F * this.floatVal6,
                0.5F,
                UiTheme.computeInt2(
                        UiTheme.computeInt(UiTheme.intVal, 0.078F + 0.09F * local5284_11), UiTheme.intVal34, local5284_10
                )
        );
        FontText.processData3(
                local5284_10 > 0.5F ? FontFace.SEMI_BOLD : FontFace.SEMI,
                accountEntry.formatText4(),
                local5284_9 + 20.5F * this.floatVal6,
                local5284_3 + 5.25F * this.floatVal6,
                6.0F * this.floatVal6,
                UiTheme.computeInt2(
                        UiTheme.computeInt2(UiTheme.intVal28, UiTheme.intVal, local5284_11),
                        UiTheme.intVal,
                        local5284_10
                )
        );
        this.list.add(
                new DataRecord2(
                        local5284_9, local5284_3, 218.5F * this.floatVal6, 16.5F * this.floatVal6, true, () -> this.accountStore.processData5(accountEntry)
                )
        );
        Render2D.updateState();
    }

    public boolean matchesCriteria4(int local5287_1) {
        if (local5287_1 >= 32 && local5287_1 <= 65535 && this.nested0482 != TypeOption.NONE) {
            char local5287_2 = (char)local5287_1;
            if (this.nested0482 == TypeOption.SEARCH) {
                if (this.flag) {
                    this.strVal = "";
                    this.flag = false;
                }

                if (this.strVal.length() < 24) {
                    this.strVal = this.strVal + local5287_2;
                }
            } else {
                if (this.flag) {
                    this.strVal2 = "";
                    this.flag = false;
                }

                if (this.strVal2.length() < 16) {
                    this.strVal2 = this.strVal2 + local5287_2;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public void renderElement3(GuiGraphicsExtractor drawContext, float local5288_2, float local5288_3, float local5288_4) {
        float local5288_5 = this.computeFloat(2);
        if (!(local5288_5 < 0.05F)) {
            float local5288_6 = 1.0F;
            float local5288_7 = local5288_2 + 51.5F * local5288_3;
            float local5288_8 = local5288_7 - (1.0F - local5288_5) * 14.0F * local5288_3;
            float local5288_9 = this.floatVal4 - this.computeFloat3(this.floatVal2) * local5288_3;

            for (AccountEntry accountEntry : this.list2) {
                if (local5288_9 + 16.5F * local5288_3 >= this.floatVal4 - 2.0F * local5288_3 && local5288_9 <= this.floatVal5 + 2.0F * local5288_3) {
                    float local5288_12 = Math.max(local5288_9 + 1.5F * local5288_3, this.floatVal4);
                    float local5288_13 = Math.min(local5288_9 + 15.0F * local5288_3, this.floatVal5);
                    if (local5288_13 - local5288_12 > 2.0F * local5288_3) {
                        Render2D.pushScissorBounds(
                                Math.round(local5288_8 + 3.5F * local5288_3),
                                Math.round(local5288_12),
                                Math.round(local5288_8 + 19.5F * local5288_3),
                                Math.round(local5288_13)
                        );

                        try {
                            AccountPreviewRenderer.renderElement(
                                    drawContext,
                                    accountEntry,
                                    local5288_6,
                                    local5288_8 + 3.5F * local5288_3,
                                    local5288_9 + 1.5F * local5288_3,
                                    14.0F * local5288_3 * local5288_5
                            );
                        } catch (RuntimeException ignored) {
                        }

                        Render2D.popScissor();
                    }
                }

                local5288_9 += 22.5F * local5288_3;
            }

            AccountEntry accountEntry2 = this.accountStore.getAccountEntry();
            if (accountEntry2 != null) {
                float local5288_11 = this.computeFloat(3);
                if (!(local5288_11 < 0.05F)) {
                    float local5288_12 = this.__ == 0L ? 1.0F : UiTheme.computeFloat2(UiTheme.computeFloat3(this.__, 0L, 340L));
                    float local5288_13 = local5288_7 - (1.0F - local5288_11) * 14.0F * local5288_3;
                    float local5288_14 = local5288_4 + 372.5F * local5288_3;
                    float local5288_15 = 26.0F * local5288_3 * local5288_11 * (0.75F + 0.25F * local5288_12);
                    Render2D.pushScissorBounds(
                            Math.round(local5288_13 + 4.5F * local5288_3),
                            Math.round(local5288_14 + 1.0F * local5288_3),
                            Math.round(local5288_13 + 33.5F * local5288_3),
                            Math.round(local5288_14 + 31.5F * local5288_3)
                    );

                    try {
                        AccountPreviewRenderer.renderElement(
                                drawContext,
                                accountEntry2,
                                local5288_6,
                                local5288_13 + 6.0F * local5288_3,
                                local5288_14 + 3.0F * local5288_3,
                                local5288_15
                        );
                    } catch (RuntimeException ignored) {
                    }

                    Render2D.popScissor();
                }
            }
        }
    }

    public enum TypeOption {
        NONE,
        SEARCH,
        CREATE,
        RENAME
    }

    private record DataRecord2(float x, float y, float w, float h, boolean clipped, Runnable action) {
    }
}
