import axiosInstance from '../../environment/axiosInstance';

export const postPoll = async (pollDTO) => {
    try {
        const response = await axiosInstance.post('api/user/poll', pollDTO);
        return response;
    } catch (error) {
        throw error;
    }
};

export const getAllPolls = async () => {
    try {
        const response = await axiosInstance.get('api/user/polls');
        return response;
    } catch (error) {
        throw error;
    }
};

export const getMyPolls = async () => {
    try {
        const response = await axiosInstance.get('api/user/my-polls');
        return response;
    } catch (error) {
        throw error;
    }
};

export const deletePollById= async (id) => {
    try {
        const response = await axiosInstance.delete(`api/user/poll/${id}`);
        return response;
    } catch (error) {
        throw error;
    }
};

export const getPollById= async (pollId) => {
    try {
        const response = await axiosInstance.get(`api/user/poll/${pollId}`);
        return response;
    } catch (error) {
        throw error;
    }
};


export const giveLikeToPoll= async (pollId) => {
    try {
        const response = await axiosInstance.get(`api/user/poll/like/${pollId}`);
        return response;
    } catch (error) {
        console.log("Error whild adding like : ",error);
        throw error;
    }
};


export const postCommentOnPoll= async (commentDTO) => {
    try {
        const response = await axiosInstance.post(`api/user/poll/comment`,commentDTO);
        return response;
    } catch (error) {
        console.log("Error while posting comment",error);
        throw error;
    }
};

export const postVoteOnPoll= async (obj) => {
    try {
        const response = await axiosInstance.post(`api/user/poll/vote`,obj);
        return response;
    } catch (error) {
        console.log("Error while posting Vote",error);
        throw error;
    }
};
