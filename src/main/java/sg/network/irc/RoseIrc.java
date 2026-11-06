package sg.network.irc;

import com.google.gson.annotations.SerializedName;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.ScheduledFuture;
import java.nio.channels.ClosedChannelException;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import sg.network.irc.IrcEncryptionPacket;
import sg.network.irc.IrcInboundHandler;
import sg.network.irc.IrcCustomPacketDispatcher;
import sg.network.irc.codec.IrcPacketEncoder;
import sg.network.irc.codec.IrcFrameDecoder;

public class RoseIrc implements AutoCloseable {
   private static volatile RoseIrc Х;
   @SerializedName("В")
   private final IrcClientState В_677a0540 = new IrcClientState();
   @SerializedName("В")
   private final IrcConnectionContext В_d285c1ed = new IrcConnectionContext();
   @SerializedName("В")
   private final EventLoopGroup В_3b6b8683 = new NioEventLoopGroup(1, new DefaultThreadFactory("rose-irc", true));
   private final String Ящ;
   private final int Дб;
   private volatile Channel А;
   private volatile boolean еф;
   private volatile boolean еи;
   private volatile boolean е9;
   private volatile String ЯГ = "";
   private boolean ео;
   private int ДЫ;
   private ScheduledFuture<?> Я;

   public RoseIrc(String var1, int var2, String var3, String var4) {
      this(var1, var2, var3, var4, true);
   }

   public String Й0() {
      return this.ЯГ;
   }

   public boolean Щ6() {
      return !this.еи && !this.е9 && this.еф && this.А != null && this.А.isActive();
   }

   private void жТ() {
      if (!this.еи && !this.е9 && !this.ео && (this.А == null || !this.А.isActive())) {
         if (this.Я != null) {
            this.Я.cancel(false);
         }

         this.Я = null;
         this.ео = true;
         this.еф = false;
         new Bootstrap()
            .group(this.В_3b6b8683)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .handler(
               new ChannelInitializer<NioSocketChannel>() {
                  protected void initChannel(final NioSocketChannel var1) {
                     if (!RoseIrc.this.еи && !RoseIrc.this.е9) {
                        RoseIrc.this.А = var1;
                        RoseIrc.this.В_d285c1ed.е(var1);
                        final ChannelPipeline var2 = var1.pipeline();
                        RoseIrc.this.В_d285c1ed.ч(var2);
                        var2.addLast("pause", new ChannelDuplexHandler() {
                           @Override
                           public void channelRead(ChannelHandlerContext var1, Object var2x) throws Exception {
                              if (!RoseIrc.this.е9 && !RoseIrc.this.еи) {
                                 super.channelRead(var1, var2x);
                              } else {
                                 ReferenceCountUtil.release(var2x);
                              }
                           }

                           @Override
                           public void write(ChannelHandlerContext var1, Object var2x, ChannelPromise var3) throws Exception {
                              if (!RoseIrc.this.е9 && !RoseIrc.this.еи) {
                                 super.write(var1, var2x, var3);
                              } else {
                                 ReferenceCountUtil.release(var2x);
                                 var3.tryFailure(new ClosedChannelException());
                              }
                           }
                        });
                        var2.addLast(new LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4));
                        var2.addLast(new LengthFieldPrepender(4));
                        var2.addLast("inbound", new IrcFrameDecoder());
                        var2.addLast(
                           "connection",
                           new ChannelInboundHandlerAdapter() {
                              @SerializedName("В")
                              private ScheduledFuture<?> В_f81f90be;

                              @Override
                              public void channelActive(ChannelHandlerContext var1x) throws Exception {
                                 if (!RoseIrc.this.еи && !RoseIrc.this.е9) {
                                    this.В_f81f90be = var1x.executor().schedule(() -> {
                                       if (!RoseIrc.this.еф) {
                                          RoseIrc.this.ЯГ = "IRC authorization timed out";
                                          var1x.close();
                                       }
                                    }, 15L, TimeUnit.SECONDS);
                                    super.channelActive(var1x);
                                 } else {
                                    var1x.close();
                                 }
                              }

                              @Override
                              public void channelInactive(ChannelHandlerContext var1x) throws Exception {
                                 if (this.В_f81f90be != null) {
                                    this.В_f81f90be.cancel(false);
                                 }

                                 if (RoseIrc.this.А == var1) {
                                    RoseIrc.this.еф = false;
                                    RoseIrc.this.ео = false;
                                    RoseIrc.this.жП();
                                 }

                                 super.channelInactive(var1x);
                              }

                              @Override
                              public void channelRead(ChannelHandlerContext var1x, Object var2x) throws Exception {
                                 if (!RoseIrc.this.е9 && !RoseIrc.this.еи) {
                                    super.channelRead(var1x, var2x);
                                    if (var2x instanceof IrcEncryptionPacket
                                       && var2.get("encrypt") != null
                                       && var2.get("decrypt") != null
                                       && var1.isActive()
                                       && !RoseIrc.this.е9
                                       && !RoseIrc.this.еи) {
                                       RoseIrc.this.еф = true;
                                       RoseIrc.this.ЯГ = "";
                                       RoseIrc.this.ДЫ = 0;
                                       if (this.В_f81f90be != null) {
                                          this.В_f81f90be.cancel(false);
                                       }
                                    }
                                 } else {
                                    ReferenceCountUtil.release(var2x);
                                 }
                              }
                           }
                        );
                        var2.addLast("main", new IrcInboundHandler());
                        var2.addLast("custom", new IrcCustomPacketDispatcher());
                        var2.addLast("outbound", new IrcPacketEncoder());
                        var2.addLast("errors", new ChannelInboundHandlerAdapter() {
                           @Override
                           public void exceptionCaught(ChannelHandlerContext var1, Throwable var2x) {
                              RoseIrc.this.ЯГ = var2x.getClass().getSimpleName() + ": " + var2x.getMessage();
                              var1.close();
                           }
                        });
                     } else {
                        var1.close();
                     }
                  }
               }
            )
            .connect(this.Ящ, this.Дб)
            .addListener(var1 -> {
               this.ео = false;
               if (!var1.isSuccess()) {
                  this.ЯГ = String.valueOf(var1.cause());
                  this.жП();
               }
            });
      }
   }

   public void ьЮ(boolean var1) {
      if (!this.еи && this.е9 != var1) {
         this.е9 = var1;
         if (!var1) {
            this.жв();
         } else {
            this.еф = false;

            try {
               this.В_3b6b8683.next().execute(() -> {
                  if (this.е9) {
                     if (this.Я != null) {
                        this.Я.cancel(false);
                     }

                     this.Я = null;
                     if (this.А != null) {
                        this.А.close();
                     }
                  }
               });
            } catch (RejectedExecutionException var3) {
               if (!this.еи) {
                  throw var3;
               }
            }
         }
      }
   }

   public boolean Щl() {
      return this.е9;
   }

   public IrcClientState З_677a0540() {
      return this.В_677a0540;
   }

   public static RoseIrc З_3e6e5516() {
      return Х;
   }

   public void жв() {
      if (!this.еи && !this.е9) {
         try {
            this.В_3b6b8683.next().execute(this::жТ);
         } catch (RejectedExecutionException var2) {
            if (!this.еи) {
               throw var2;
            }
         }
      }
   }

   public IrcConnectionContext З_d285c1ed() {
      return this.В_d285c1ed;
   }

   public Channel Й() {
      return this.е9 ? null : this.А;
   }

   private void жП() {
      if (!this.еи && !this.е9 && (this.Я == null || this.Я.isDone())) {
         long var1 = Math.min(30L, 1L << Math.min(this.ДЫ++, 5));
         this.Я = this.В_3b6b8683.next().schedule(this::жТ, var1, TimeUnit.SECONDS);
      }
   }

   @Override
   public void close() {
      this.еи = true;
      this.еф = false;
      if (this.А != null) {
         this.А.close();
      }

      this.В_3b6b8683.shutdownGracefully(0L, 2L, TimeUnit.SECONDS);
   }

   public RoseIrc(String var1, int var2, String var3, String var4, boolean var5) {
      this.Ящ = var1;
      this.Дб = var2;
      this.В_d285c1ed.Т(var3);
      this.В_d285c1ed.Ъ(var4);
      RoseIrc var6 = Х;
      if (var6 != null) {
         var6.close();
      }

      Х = this;
      if (var5) {
         this.жв();
      }
   }
}
