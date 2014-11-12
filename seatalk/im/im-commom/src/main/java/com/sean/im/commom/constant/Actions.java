package com.sean.im.commom.constant;

public class Actions
{
	// Server Action
	public static final String LoginAction = "LoginAction";
	public static final String UpdateInfoAction = "UpdateInfoAction";
	public static final String UpdateRemarkAction = "UpdateRemarkAction";
	public static final String ChangeStatusAction = "ChangeStatusAction";
	public static final String InquireInfoAction = "InquireInfoAction";
	public static final String InquireInfoBriefAction = "InquireInfoBriefAction";
	public static final String MoveFriendAction = "MoveFriendAction";
	public static final String DeleteFriendAction = "DeleteFriendAction";
	public static final String RenameGroupAction = "RenameGroupAction";
	public static final String AddGroupAction = "AddGroupAction";
	public static final String DeleteGroupAction = "DeleteGroupAction";
	public static final String ChangePasswordAction = "ChangePasswordAction";
	public static final String SendMessageAction = "SendMessageAction";
	public static final String SendImageAction = "SendImageAction";
	public static final String SendAudioAction = "SendAudioAction";
	public static final String SendFileAction = "SendFileAction";
	public static final String SearchUserAction = "SearchUserAction";
	public static final String RequestFriendAction = "RequestFriendAction";
	public static final String HandleRequestFriendAction = "HandleRequestFriendAction";
	public static final String InquireFriendAction = "InquireFriendAction";
	public static final String InquireUnReadMsgAction = "InquireUnReadMsgAction";
	public static final String InquireFriendListAction = "InquireFriendListAction";
	public static final String InquireChatRecordAction = "InquireChatRecordAction";
	public static final String DeleteChatRecordAction = "DeleteChatRecordAction";
	public static final String InquireServerInfoAction = "InquireServerInfoAction";
	public static final String RegistAction = "RegistAction";
	public static final String ExitClientAction = "ExitClientAction";
	public static final String ChangeTranslatorAction = "ChangeTranslatorAction";
	public static final String SendTrembleAction = "SendTrembleAction";
	
	// Client Action
	public static final String StatusChangedHandler = "StatusChangedHandler";
	public static final String DeleteFriendHandler = "DeleteFriendHandler";
	public static final String ReceiveMsgHandler = "ReceiveMsgHandler";
	public static final String ReceiveWarnMsgHandler = "ReceiveWarnMsgHandler";
	public static final String TrembleHandler = "TrembleHandler";
	public static final String RequestFriendHandler = "RequestFriendHandler";
	public static final String RequestFriendResultHandler = "RequestFriendResultHandler";
	public static final String ExitHandler = "ExitHandler";
	public static final String ReceiveFileHandler = "ReceiveFileHandler";
	public static final String ReceiveFlockMsgHandler = "ReceiveFlockMsgHandler";
	public static final String ReceiveFlockFileHandler = "ReceiveFlockFileHandler";

	// action of flock module
	public static final String CreateFlockAction = "CreateFlockAction";
	public static final String InquireFlockListAction = "InquireFlockListAction";
	public static final String InquireFlockBriefAction = "InquireFlockBriefAction";
	public static final String InquireFlockInfoAction = "InquireFlockInfoAction";
	public static final String InquireMemberListAction = "InquireMemberListAction";
	public static final String AddFlockMemberAction = "AddFlockMemberAction";
	public static final String SendFlockMessageAction = "SendFlockMessageAction";
	public static final String SendFlockImageAction = "SendFlockImageAction";
	public static final String SendFlockAudioAction = "SendFlockAudioAction";
	public static final String SendFlockFileAction = "SendFlockFileAction";
	public static final String InquireFlockCardAction = "InquireFlockCardAction";
	public static final String UpdateFlockCardAction = "UpdateFlockCardAction";
	public static final String UpdateFlockInfoAction = "UpdateFlockInfoAction";
	public static final String GrantFlockAdminAction = "GrantFlockAdminAction";
	public static final String TakeBackFlockAdminAction = "TakeBackFlockAdminAction";
	public static final String KickOutFlockAction = "KickOutFlockAction";
	public static final String ExitFlockAction = "ExitFlockAction";
	public static final String DismissFlockAction = "DismissFlockAction";
	
	// push of flock module
	public static final String GrantOrTakeBackFlockAdminHandler  = "GrantOrTakeBackFlockAdminHandler";
	public static final String KickOutFlockHandler  = "KickOutFlockHandler";
	public static final String JoinInFlockHandler  = "JoinInFlockHandler";
	public static final String DismissFlockHandler  = "DismissFlockHandler";
}
