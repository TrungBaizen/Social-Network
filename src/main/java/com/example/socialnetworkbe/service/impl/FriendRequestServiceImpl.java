//package com.example.socialnetworkbe.service.impl;
//
//import com.example.socialnetworkbe.model.Friend;
//import com.example.socialnetworkbe.model.FriendRequest;
//import com.example.socialnetworkbe.model.User;
//import com.example.socialnetworkbe.repository.FriendRepository;
//import com.example.socialnetworkbe.repository.FriendRequestRepository;
//import com.example.socialnetworkbe.repository.UserRepository;
//import com.example.socialnetworkbe.service.FriendRequestService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class FriendRequestServiceImpl implements FriendRequestService {
//
//    private final FriendRequestRepository friendRequestRepository;
//    private final FriendRepository friendRepository;
//    private final UserRepository userRepository;
//
//    @Autowired
//    public FriendRequestServiceImpl(FriendRequestRepository friendRequestRepository,
//                                    FriendRepository friendRepository,
//                                    UserRepository userRepository) {
//        this.friendRequestRepository = friendRequestRepository;
//        this.friendRepository = friendRepository;
//        this.userRepository = userRepository;
//    }
//
//
//    @Override
//    public FriendRequest sendFriendRequest(Long senderId, Long receiverId) {
//        // Kiểm tra xem người gửi và người nhận có tồn tại trong cơ sở dữ liệu không
//        User sender = userRepository.findById(senderId)
//                .orElseThrow(() -> new RuntimeException("Sender not found."));
//        User receiver = userRepository.findById(receiverId)
//                .orElseThrow(() -> new RuntimeException("Receiver not found."));
//
//        if (senderId == receiverId) {
//            throw new RuntimeException("Friend request already sent.");
//         }
//
//        // Kiểm tra xem người gửi và người nhận đã có mối quan hệ kết bạn nào chưa
//        Optional<Friend> existingFriendship = friendRepository.findByUserAndFriendUser(sender, receiver);
//        if (existingFriendship.isPresent()) {
//            throw new RuntimeException("You are already friends.");
//        }
//
//        // Kiểm tra xem đã có yêu cầu kết bạn nào từ người gửi đến người nhận hoặc ngược lại chưa
//        Optional<FriendRequest> existingRequestToReceiver = friendRequestRepository.findBySenderIdAndReceiverId(senderId, receiverId);
//        Optional<FriendRequest> existingRequestFromReceiver = friendRequestRepository.findBySenderIdAndReceiverId(receiverId, senderId);
//
//        if (existingRequestToReceiver.isPresent()) {
//            throw new RuntimeException("Friend request already sent to the receiver.");
//        }
//
//        if (existingRequestFromReceiver.isPresent()) {
//            throw new RuntimeException("A request from the receiver is already pending.");
//        }
//
//        FriendRequest friendRequest = new FriendRequest();
//        friendRequest.setSender(sender);
//        friendRequest.setReceiver(receiver);
//        friendRequest.setFollow(true); // A follows B
//        friendRequest.setAccepted(false);
//
//        return friendRequestRepository.save(friendRequest);
//    }
//
//    @Override
//    public FriendRequest acceptFriendRequest(Long requestId) {
//        // Tìm yêu cầu kết bạn theo ID
//        FriendRequest friendRequest = friendRequestRepository.findById(requestId)
//                .orElseThrow(() -> new RuntimeException("Friend request not found."));
//
//        // Đánh dấu yêu cầu kết bạn là đã được chấp nhận
//        friendRequest.setAccepted(true);
//
//        // Kiểm tra xem đã có quan hệ bạn bè giữa người gửi và người nhận chưa
//        Optional<Friend> existingFriendA = friendRepository.findByUserAndFriendUser(
//                friendRequest.getSender(), friendRequest.getReceiver());
//        Optional<Friend> existingFriendB = friendRepository.findByUserAndFriendUser(
//                friendRequest.getReceiver(), friendRequest.getSender());
//
//        if (!existingFriendA.isPresent() && !existingFriendB.isPresent()) {
//            // Tạo đối tượng Friend cho người gửi (người tạo yêu cầu kết bạn)
//            Friend userSendRequest = new Friend();
//            userSendRequest.setUser(friendRequest.getSender());
//            userSendRequest.setFriendUser(friendRequest.getReceiver());
//            userSendRequest.setFollow(true);
//            userSendRequest.setAccepted(true);
//
//            friendRepository.save(userSendRequest);
//
//            // Tạo đối tượng Friend cho người nhận (người nhận yêu cầu kết bạn)
//            Friend userAccept = new Friend();
//            userAccept.setUser(friendRequest.getReceiver());
//            userAccept.setFriendUser(friendRequest.getSender());
//            userAccept.setFollow(true);
//            userAccept.setAccepted(true);
//
//            friendRepository.save(userAccept);
//        }
//
//        // Lưu yêu cầu kết bạn đã được chấp nhận và xóa yêu cầu kết bạn từ bảng
//        friendRequestRepository.save(friendRequest);
//        friendRequestRepository.delete(friendRequest);
//
//        return friendRequest;
//    }
//
//
//    @Override
//    public FriendRequest rejectFriendRequest(Long requestId) {
//        // Tìm yêu cầu kết bạn theo ID
//        FriendRequest friendRequest = friendRequestRepository.findById(requestId)
//                .orElseThrow(() -> new RuntimeException("Friend request not found."));
//
//        // Đánh dấu yêu cầu kết bạn là đã bị từ chối
//        friendRequest.setAccepted(false);
//
//        // Lưu trạng thái từ chối yêu cầu kết bạn
//        friendRequestRepository.save(friendRequest);
//
//        // Xóa yêu cầu kết bạn khỏi bảng
//        friendRequestRepository.delete(friendRequest);
//
//        return friendRequest;
//    }
//
//
//
//
//    @Override
//    public List<FriendRequest> getPendingRequestsForUser(Long userId) {
//        return friendRequestRepository.findByReceiverId(userId);
//    }
//
//    @Override
//    public Optional<FriendRequest> getRequest(Long senderId, Long receiverId) {
//        return friendRequestRepository.findBySenderIdAndReceiverId(senderId, receiverId);
//    }
//
//    @Override
//    public void rejectRequest(Long requestId) {
//        friendRequestRepository.deleteById(requestId);
//    }
//}



package com.example.socialnetworkbe.service.impl;

import com.example.socialnetworkbe.model.*;
import com.example.socialnetworkbe.model.DTO.FriendRequestDTO;
import com.example.socialnetworkbe.model.DTO.FriendshipStatusDTO;
import com.example.socialnetworkbe.model.DTO.UserDTO;
import com.example.socialnetworkbe.repository.FollowRepository;
import com.example.socialnetworkbe.repository.FriendRepository;
import com.example.socialnetworkbe.repository.FriendRequestRepository;
import com.example.socialnetworkbe.repository.UserRepository;
import com.example.socialnetworkbe.service.FollowService;
import com.example.socialnetworkbe.service.FriendRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FriendRequestServiceImpl implements FriendRequestService {

    private static final Logger log = LoggerFactory.getLogger(FriendRequestServiceImpl.class);
    private final FriendRequestRepository friendRequestRepository;
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;



    @Autowired
    public FriendRequestServiceImpl(FriendRequestRepository friendRequestRepository,
                                    FriendRepository friendRepository,
                                    UserRepository userRepository,
                                    FollowRepository followRepository) {
        this.friendRequestRepository = friendRequestRepository;
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
        this.followRepository = followRepository;

    }

    @Override
    public FriendRequest sendFriendRequest(Long senderId, Long receiverId) {
        // Kiểm tra xem người gửi và người nhận có tồn tại trong cơ sở dữ liệu không
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found."));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found."));

        if (senderId.equals(receiverId)) {
            throw new RuntimeException("Cannot send a friend request to yourself.");
        }

        // Kiểm tra xem người gửi và người nhận đã có mối quan hệ kết bạn nào chưa
        Optional<Friend> existingFriendship = friendRepository.findByUserAndFriendUser(sender, receiver);
        if (existingFriendship.isPresent()) {
            throw new RuntimeException("You are already friends.");
        }

        // Kiểm tra xem đã có yêu cầu kết bạn nào từ người gửi đến người nhận hoặc ngược lại chưa
        Optional<FriendRequest> existingRequestToReceiver = friendRequestRepository.findBySenderIdAndReceiverId(senderId, receiverId);
        Optional<FriendRequest> existingRequestFromReceiver = friendRequestRepository.findBySenderIdAndReceiverId(receiverId, senderId);

        if (existingRequestToReceiver.isPresent()) {
            throw new RuntimeException("Friend request already sent to the receiver.");
        }

        if (existingRequestFromReceiver.isPresent()) {
            throw new RuntimeException("A request from the receiver is already pending.");
        }

        // Tạo và lưu yêu cầu kết bạn
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(sender);
        friendRequest.setReceiver(receiver);
        friendRequest.setFollow(true);
        friendRequest.setAccepted(false);

        friendRequestRepository.save(friendRequest);

        // Lưu thông tin follow giữa người gửi và người nhận
        Follow follow = new Follow();
        follow.setFollower(sender);
        follow.setFollowed(receiver);
        followRepository.save(follow);

        return friendRequest;
    }






    @Override
    public FriendRequest acceptFriendRequest(Long requestId) {
        // Tìm yêu cầu kết bạn theo ID
        FriendRequest friendRequest = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found."));

        // Đánh dấu yêu cầu kết bạn là đã được chấp nhận
        friendRequest.setAccepted(true);

        // Kiểm tra xem đã có quan hệ bạn bè giữa người gửi và người nhận chưa
        Optional<Friend> existingFriendA = friendRepository.findByUserAndFriendUser(
                friendRequest.getSender(), friendRequest.getReceiver());
        Optional<Friend> existingFriendB = friendRepository.findByUserAndFriendUser(
                friendRequest.getReceiver(), friendRequest.getSender());

        if (!existingFriendA.isPresent() && !existingFriendB.isPresent()) {
            // tạo đối tượng Friend cho người gửi (người tạo yêu cầu kết bạn)
            Friend userSendRequest = new Friend();
            userSendRequest.setUser(friendRequest.getSender());
            userSendRequest.setFriendUser(friendRequest.getReceiver());
            userSendRequest.setFollow(true);
            userSendRequest.setAccepted(true);

            friendRepository.save(userSendRequest);

            // tạo đối tượng Friend cho người nhận (người nhận yêu cầu kết bạn)
            Friend userAccept = new Friend();
            userAccept.setUser(friendRequest.getReceiver());
            userAccept.setFriendUser(friendRequest.getSender());
            userAccept.setFollow(true);
            userAccept.setAccepted(true);

            friendRepository.save(userAccept);
        }

        // Lưu yêu cầu kết bạn đã được chấp nhận và xóa yêu cầu kết bạn từ bảng
        friendRequestRepository.save(friendRequest);
        friendRequestRepository.delete(friendRequest);

        // Lưu thông tin follow cho người nhận
        Follow follow = new Follow();
        follow.setFollower(friendRequest.getReceiver());
        follow.setFollowed(friendRequest.getSender());
        followRepository.save(follow);

        return friendRequest;
    }


    @Override
    public FriendRequest rejectFriendRequest(Long requestId) {
        // Tìm yêu cầu kết bạn theo ID
        FriendRequest friendRequest = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found."));

        // Đánh dấu yêu cầu kết bạn là đã bị từ chối
        friendRequest.setAccepted(false);

        // Xóa yêu cầu kết bạn khỏi bảng
        friendRequestRepository.delete(friendRequest);

        return friendRequest;
    }



    @Override
    @Transactional
    public void cancelFriendRequest(Long senderId, Long receiverId) {
        // Tìm yêu cầu kết bạn
        Optional<FriendRequest> requestOpt = friendRequestRepository.findBySenderIdAndReceiverId(senderId, receiverId);

        if (requestOpt.isPresent()) {
            FriendRequest request = requestOpt.get();

            // Xóa yêu cầu kết bạn
            friendRequestRepository.delete(request);

            // Xóa hoặc cập nhật mối quan hệ follow
            Optional<Follow> followOpt = followRepository.findByFollowerIdAndFollowedId(senderId, receiverId);

            followOpt.ifPresent(follow -> {
                followRepository.delete(follow); // Xóa mối quan hệ follow
            });

            System.out.println("Cancelled friend request from " + senderId + " to " + receiverId);
        } else {
            throw new RuntimeException("Friend request not found");
        }
    }

//    @Override
//    public List<FriendRequest> getPendingRequestsForUser(Long userId) {
//        return friendRequestRepository.findByReceiverId(userId);
//    }

    @Override
    public List<FriendRequestDTO> getPendingRequestsForUser(Long userId) {
        List<FriendRequest> requests = friendRequestRepository.findByReceiverId(userId);

        return requests.stream().map(request -> {
            User sender = request.getSender();
            User receiver = request.getReceiver();

            // Tạo và trả về đối tượng FriendRequestDTO
            return new FriendRequestDTO(
                    request.getId(),
                    sender.getId(),
                    sender.getEmail(),
                    sender.getFirstName(),
                    sender.getLastName(),
                    receiver.getId(),
                    receiver.getEmail(),
                    receiver.getFirstName(),
                    receiver.getLastName(),
                    request.getCreateTime(),
                    request.getUpdateTime(),
                    request.isFollow(),
                    request.isAccepted()
            );
        }).collect(Collectors.toList());
    }



}
